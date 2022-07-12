package it.jgardner.format;

import it.jgardner.format.block.Block;
import it.jgardner.format.block.PrimitiveBlock;
import it.jgardner.format.block.Recogniser;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenList {

    public List<Token> tokens;
    public int index = 0;
    public List<ConditionalBranch> conditionalBranch = List.of();

    public TokenList(List<Token> tokens) {
        Stack<ConditionalBranch> conditionalBranch = new Stack<>();
        Stack<Integer> blockCounts = new Stack<>();
        blockCounts.push(0);
        int index = 0;
        for (Token token : tokens) {
            if (token.reservedType == ReservedType.IfDef) {
                blockCounts.push(0);
                conditionalBranch.push(new ConditionalBranch(blockCounts.peek()));
                token.conditionalDepth = -1;
            } else if (token.reservedType == ReservedType.Else) {
                conditionalBranch.push(new ConditionalBranch(blockCounts.peek(), conditionalBranch.pop().conditionalBranchIndex + 1));
                token.conditionalDepth = -1;
            } else if (token.reservedType == ReservedType.EndIf) {
                conditionalBranch.pop();
                blockCounts.pop();
                blockCounts.push(blockCounts.pop() + 1);
                token.conditionalDepth = -1;
            } else {
                token.conditionalDepth = conditionalBranch.size();
                token.conditionalBranch = conditionalBranch.stream().toList();
            }
            token.index = index++;
        }
        this.tokens = tokens;
    }

    public TokenList(TokenList source) {
        this.tokens = source.tokens;
        this.index = source.index;
        this.conditionalBranch = source.conditionalBranch;
    }

    public Token getNextToken() {
        return tokens.get(index);
    }

    private boolean nextTokenIsRelevant(List<ConditionalBranch> conditionalBranch) {
        if (index >= tokens.size()) {
            return false;
        }
        Token nextToken = getNextToken();
        if (List.of(ReservedType.IfDef, ReservedType.Else, ReservedType.EndIf).contains(nextToken.reservedType)) {
            return false;
        }

        List<ConditionalBranch> tokenBranches = nextToken.conditionalBranch;
        if (conditionalBranch.size() == 0) {
            return tokenBranches.stream().allMatch(branch -> branch.conditionalBranchIndex == 0);
        }

        for (int conditionalIndex = 0; conditionalIndex < Math.min(tokenBranches.size(), conditionalBranch.size()); conditionalIndex++) {
            ConditionalBranch tokenBranch = tokenBranches.get(conditionalIndex);
            ConditionalBranch blockBranch = conditionalBranch.get(conditionalIndex);
            if (tokenBranch.conditionalBlockIndex != blockBranch.conditionalBlockIndex) {
                return tokenBranches
                        .stream()
                        .skip(index)
                        .allMatch(branch -> branch.conditionalBranchIndex == 0);
            }
            if (tokenBranch.conditionalBranchIndex != blockBranch.conditionalBranchIndex) {
                return false;
            }
        }
        return true;
    }

    private List<ConditionalBranch> findFirstConditionalBranch() {
        if (index >= tokens.size()) {
            return List.of();
        }
        return tokens
                .stream()
                .skip(index)
                .map(token -> token.conditionalBranch)
                .filter(branch -> branch.size() > tokens.get(index).conditionalBranch.size())
                .findFirst()
                .orElse(tokens.get(index).conditionalBranch);
    }

    private boolean existsTokenOnBranch(List<ConditionalBranch> branch) {
        return tokens
                .stream()
                .skip(index)
                .filter(token -> token.conditionalDepth >= branch.size())
                .anyMatch(token -> token.conditionalBranch.subList(0, branch.size()).equals(branch));
    }

    private TokenList createClone() {
        return new TokenList(this);
    }

    public Optional<? extends Block> recognise(Recogniser recogniser) {
        while (!nextTokenIsRelevant(conditionalBranch) && index < tokens.size()) {
            index++;
        }

        // if block doesnt contain all of ifdef, parse again with other branch of code

        //                 0 - 0            0-1                                2                2-0     2-1             3
        // Begin S1; IFDEF End Begin S2; ELSE S3; ENDIF End || Begin S1; IFDEF End Begin IFDEF S2; ELSE S4; ENDIF ELSE S3; ENDIF End
        // Begin S1; End Begin S2;  End
        // Begin S1; S3;            End
        // Token.ifdefbranch, if not all are from the same branch
        // re parse on other branch

//            System.out.println(tokens.get(0).content);
        // get all branches of code and recognise those and return conditional block with all internal blocks


        Optional<? extends Block> optionalBlock = recogniser.recognise(createClone());
        if (optionalBlock.isPresent()) {
            Block block = optionalBlock.get();

            if (block.isSurroundingBlock()) {
                // TODO: ensure this branch is only called once
                // TODO: create stack of current conditional branch being parsed

                // TODO: assert index is the same afterward - assert all blocks have same tokenDelta
                // Test for if if, end else, and end end
                System.out.println("Surrounding Block");
                block.tokens.stream().map(token -> token.content).forEach(System.out::println);

                List<ConditionalBranch> originalConditionalBranch = findFirstConditionalBranch();
                ConditionalBranch lastBranch = originalConditionalBranch.get(originalConditionalBranch.size() - 1);
                ConditionalBranch changingBranch = new ConditionalBranch(lastBranch.conditionalBlockIndex,
                        lastBranch.conditionalBranchIndex + 1);
                conditionalBranch = Stream.concat(
                                originalConditionalBranch.subList(0, originalConditionalBranch.size() - 1).stream(),
                                Stream.of(changingBranch))
                        .collect(Collectors.toList());
                System.out.println("Parsing Again");
                List<Block> reparses = new ArrayList<>();
                while (existsTokenOnBranch(conditionalBranch)) {
                    Optional<? extends Block> otherBlock = recogniser.recognise(createClone());
                    reparses.add(otherBlock.orElseThrow());
                    changingBranch.conditionalBranchIndex += 1;
                }
                if (!reparses.stream().allMatch(block1 -> block1.getTokenDelta() == block.getTokenDelta())) {
                    throw new RuntimeException("Error");
                }
                for (Block otherBlock : reparses) {
                    System.out.println(block);
                    otherBlock.tokens.forEach(System.out::println);
                }
            }
            index += block.getTokenDelta();
        }
        return optionalBlock;
    }
}

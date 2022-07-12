package it.jgardner.format.block;

import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Block implements Recogniser {

    public List<Token> tokens;
    public List<Block> children;
    public Block parent;

    @SafeVarargs
    public Block(List<Block>... children) {
        this.tokens = new ArrayList<>();
        this.children = new ArrayList<>();
        for (List<Block> blockList : children) {
            for (Block block : blockList) {
                this.children.add(block);
                block.parent = this;
                this.tokens.addAll(block.tokens);
            }
        }
    }

    public Block(List<Token> tokens) {
        this.tokens = tokens;
    }

    public abstract Optional<? extends Block> recognise(TokenList tokens);

    @Override
    public boolean isPrimitive() {
        return false;
    }

    public boolean areTokensConsecutive() {
        int actualSum = tokens.stream().map(token -> token.index).reduce(Integer::sum).orElse(0);
        int expectedSum = IntStream.range(getFirstToken().index, getFirstToken().index + tokens.size()).sum();
        return actualSum == expectedSum;
    }

    public boolean areTokensAllSameBranch() {
        return tokens.stream().allMatch(token -> token.conditionalDepth == getFirstToken().conditionalDepth);
    }

    public Token getFirstToken() {
        return tokens.get(0);
    }

    public Token getLastToken() {
        return tokens.get(tokens.size() - 1);
    }

    public int getTokenDelta() {
        return getLastToken().index - getFirstToken().index + 1;
    }

    public boolean isSurroundingBlock() {
        return !areTokensAllSameBranch() && getFirstToken().conditionalBranch == getLastToken().conditionalBranch &&
                    getFirstToken() != getLastToken();
    }
}

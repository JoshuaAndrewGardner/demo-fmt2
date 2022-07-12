package it.jgardner.format.block;

import it.jgardner.format.TokenList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompoundStatementListBlock extends AbstractBlock<CompoundStatementListBlock> {

    public CompoundStatementListBlock(List<Block> children) {
        super(children);
    }

    @Override
    public Optional<CompoundStatementListBlock> recognise(TokenList tokens) {
        List<Block> children = new ArrayList<>();
        CompoundStatementBlock compoundStatementBlockRecogniser = new CompoundStatementBlock(List.of());

        Optional<? extends Block> block = tokens.recognise(compoundStatementBlockRecogniser);
        if (block.isEmpty()) {
            return Optional.empty();
        }
        children.add(block.orElseThrow());
        block = tokens.recognise(compoundStatementBlockRecogniser);
        while (block.isPresent()) {
            children.add(block.orElseThrow());
            block = tokens.recognise(compoundStatementBlockRecogniser);
        }

        return Optional.of(new CompoundStatementListBlock(children));
    }
}

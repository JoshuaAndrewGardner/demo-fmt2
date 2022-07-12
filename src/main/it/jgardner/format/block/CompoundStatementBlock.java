package it.jgardner.format.block;

import it.jgardner.format.TokenList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompoundStatementBlock extends AbstractBlock<CompoundStatementBlock> {

    public CompoundStatementBlock(List<Block> children) {
        super(children);
    }

    @Override
    public Optional<CompoundStatementBlock> recognise(TokenList tokens) {
        List<Block> children = new ArrayList<>();
        BeginBlock beginBlockRecogniser = new BeginBlock(List.of());
        StatementListBlock statementListBlockRecogniser = new StatementListBlock(List.of());
        EndBlock endBlockRecogniser = new EndBlock(List.of());

        Optional<? extends Block> block = tokens.recognise(beginBlockRecogniser);
        if (block.isEmpty()) {
            return Optional.empty();
        }
        children.add(block.orElseThrow());
        children.add(tokens.recognise(statementListBlockRecogniser).orElseThrow());
        children.add(tokens.recognise(endBlockRecogniser).orElseThrow());

        return Optional.of(new CompoundStatementBlock(children));
    }
}

package it.jgardner.format.block;

import it.jgardner.format.ReservedType;
import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatementListBlock extends AbstractBlock<StatementListBlock> {

    public StatementListBlock(List<Block> children) {
        super(children);
    }

    @Override
    public Optional<StatementListBlock> recognise(TokenList tokens) {
        List<Block> children = new ArrayList<>();
        StatementBlock statementBlockRecogniser = new StatementBlock(List.of());
        SemiColonBlock semiColonBlockRecogniser = new SemiColonBlock(List.of());

        Optional<? extends Block> block = tokens.recognise(new StatementBlock(List.of()));
        if (block.isEmpty()) {
            return Optional.empty();
        }
        children.add(block.orElseThrow());
        children.add(tokens.recognise(semiColonBlockRecogniser).orElseThrow());

        block = tokens.recognise(statementBlockRecogniser);
        while(block.isPresent()) {
            children.add(block.orElseThrow());
            children.add(tokens.recognise(semiColonBlockRecogniser).orElseThrow());
            block = tokens.recognise(statementBlockRecogniser);
        }

        return Optional.of(new StatementListBlock(children));
    }
}

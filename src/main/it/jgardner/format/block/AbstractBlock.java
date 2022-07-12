package it.jgardner.format.block;

import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import java.util.List;
import java.util.Optional;

public abstract class AbstractBlock<T extends Block> extends Block {

    @SafeVarargs
    public AbstractBlock(List<Block>... children) {
        super(children);
    }

    public AbstractBlock(List<Token> tokens) {
        super(tokens);
    }

    public abstract Optional<T> recognise(TokenList tokens);

}

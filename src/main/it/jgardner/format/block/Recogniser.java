package it.jgardner.format.block;

import it.jgardner.format.TokenList;

import java.util.Optional;

public interface Recogniser {
    boolean isPrimitive();
    Optional<? extends Block> recognise(TokenList tokens);
}

package it.jgardner.format.block;

import it.jgardner.format.ReservedType;
import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import java.util.List;
import java.util.Optional;

public class SemiColonBlock extends PrimitiveBlock<SemiColonBlock> {

    public SemiColonBlock(List<Token> tokens) {
        super(tokens, ReservedType.SemiColon);
    }

}

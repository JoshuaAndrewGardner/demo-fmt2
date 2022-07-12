package it.jgardner.format.block;

import it.jgardner.format.ReservedType;
import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import java.util.List;
import java.util.Optional;

public class UnknownBlock extends PrimitiveBlock<UnknownBlock> {

    public UnknownBlock(List<Token> tokens) {
        super(tokens, ReservedType.Any);
    }

}

package it.jgardner.format.block;

import it.jgardner.format.ReservedType;
import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import java.util.List;
import java.util.Optional;

public class EndBlock extends PrimitiveBlock<EndBlock> {

    public EndBlock(List<Token> tokens) {
        super(tokens, ReservedType.End);
    }

}

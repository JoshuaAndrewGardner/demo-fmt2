package it.jgardner.format.block;

import it.jgardner.format.ReservedType;
import it.jgardner.format.Token;

import java.util.List;

public class BeginBlock extends PrimitiveBlock<BeginBlock> {

    public BeginBlock(List<Token> tokens) {
        super(tokens, ReservedType.Begin);
    }

}

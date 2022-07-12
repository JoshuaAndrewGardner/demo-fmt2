package it.jgardner.format.block;

import it.jgardner.format.ReservedType;
import it.jgardner.format.Token;
import it.jgardner.format.TokenList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class PrimitiveBlock<T extends Block> extends AbstractBlock<T> {

    private ReservedType reservedType;

    @SafeVarargs
    public PrimitiveBlock(List<Block>... children) {
        super(children);
    }

    public PrimitiveBlock(List<Token> tokens, ReservedType reservedType) {
        super(tokens);
        this.reservedType = reservedType;
    }

    public Optional<T> recognise(TokenList tokens) {
        if (tokens.index >= tokens.tokens.size() ||
                !List.of(ReservedType.Any, tokens.tokens.get(tokens.index).reservedType).contains(reservedType)) {
            return Optional.empty();
        }
        try {
            return Optional.of((T) this.getClass()
                    .getDeclaredConstructor(List.class)
                    .newInstance(tokens.tokens.subList(tokens.index, tokens.index + 1)));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean isPrimitive() {
        return true;
    }
}

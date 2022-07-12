package it.jgardner.format;

import java.util.List;

public class Token {

    public ReservedType reservedType;
    public String content;
    public int conditionalDepth = 0;
    public List<ConditionalBranch> conditionalBranch = List.of();
    public int index;

    public Token(ReservedType reservedType) {
        this(reservedType, reservedType.name());
    }

    public Token(ReservedType reservedType, String content) {
        this.reservedType = reservedType;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Token{" +
                "reservedType=" + reservedType +
                ", content='" + content + '\'' +
                ", conditionalDepth=" + conditionalDepth +
                ", conditionalBranch=" + conditionalBranch +
                ", index=" + index +
                '}';
    }
}

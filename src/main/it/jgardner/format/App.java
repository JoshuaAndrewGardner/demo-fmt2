package it.jgardner.format;

import it.jgardner.format.block.*;

import java.util.List;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        TokenList tokens = new TokenList(List.of(
                new Token(ReservedType.Begin),
                new Token(ReservedType.Statement, "S1"),
                new Token(ReservedType.SemiColon),
                new Token(ReservedType.IfDef),
                new Token(ReservedType.End),
                new Token(ReservedType.Begin),
                new Token(ReservedType.Statement, "S2"),
                new Token(ReservedType.SemiColon),
                new Token(ReservedType.Else),
                new Token(ReservedType.Statement, "S3"),
                new Token(ReservedType.SemiColon),
                new Token(ReservedType.EndIf),
                new Token(ReservedType.End)
        ));

        // Begin S1; IFDEF End Begin S2; ELSE S3; ENDIF End

        Optional<? extends Block> block = tokens.recognise(new CompoundStatementListBlock(List.of()));
//        Optional<? extends Block> block = tokens.recognise(new StatementListBlock(List.of()));
        while (tokens.index < tokens.tokens.size()) {
            block = tokens.recognise(new UnknownBlock(List.of()));
        }
    }
}

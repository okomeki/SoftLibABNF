package net.siisise.ebnf.parser;

import net.siisise.abnf.parser5234.ABNF5234;
import net.siisise.bnf.BNF;
import net.siisise.ebnf.EBNF;
import net.siisise.ebnf.EBNFReg;

/**
 * EBNF を ABNF で書く
 */
public class EBNFISO14977 {

    public static final EBNFReg REG = new EBNFReg();
    
    static final EBNF repetitionSymbol = REG.rule("repetition-symbol",EBNF.bin('*'));
    static final EBNF exceptSymbol = REG.rule("except-symbol",EBNF.bin('-'));
    static final EBNF concatenateSymbol = REG.rule("concatenate-symbol", EBNF.bin(','));
    static final EBNF definitionSeparatorSymbol = REG.rule("definition-separator-symbol", EBNF.bin('|'));
    static final EBNF definingSymbol = REG.rule("defining-symbol", EBNF.bin('='));
    static final EBNF terminatorSymbol = REG.rule("terminator-symbol", EBNF.bin(';'));
    
    static final EBNF firstQuoteSymbol = REG.rule("first-quote-symbol", EBNF.bin('\''));
    static final EBNF secondQuoteSymbol = REG.rule("second-quote-symbol", EBNF.bin('"'));
    static final EBNF startCommentSymbol = REG.rule("start-comment-symbol", EBNF.bin("(*"));
    static final EBNF endCommentSymbol = REG.rule("end-comment-symbol", EBNF.bin("*)"));
    static final EBNF startGroupSymbol = REG.rule("start-group-symbol", EBNF.bin("("));
    static final EBNF endGroupSymbol = REG.rule("end-group-symbol", EBNF.bin(")"));
    static final EBNF startOptionSymbol = REG.rule("start-option-symbol", EBNF.bin("["));
    static final EBNF endOptionSymbol = REG.rule("end-option-symbol", EBNF.bin("]"));
    static final EBNF startRepeatSymbol = REG.rule("start-repeat-symbol", EBNF.bin("{"));
    static final EBNF endRepeatSymbol = REG.rule("end-repeat-symbol", EBNF.bin("}"));
    static final EBNF specialSequenceSymbol = REG.rule("special-sequence-symbol", EBNF.bin("?"));
    
    static final EBNF letter = REG.rule("letter", EBNF.range('a','z').or(EBNF.range('A','Z')));
    static final EBNF decimalDigit = REG.rule("decimal-digit", EBNF.range('0','9'));
    
    /** 3.7. 6.2. */
    static final EBNF terminalCharacter = REG.rule("terminal-character", EBNF.range(0x20,0x7e));

    static final EBNF emptySequence = REG.rule("empty-sequence", terminalCharacter.x(0,0));
    
    static final EBNF specialSequenceCharacter = REG.rule("special-sequence-character", terminalCharacter.mn(specialSequenceSymbol));
    static final EBNF specialSequence = REG.rule("special-sequence", specialSequenceSymbol.pl(specialSequenceCharacter.x(), specialSequenceSymbol) );
    
    static final EBNF firstTerminalCharacter = REG.rule("first-terminal-character", terminalCharacter.mn(firstQuoteSymbol));
    static final EBNF secondTerminalCharacter = REG.rule("second-terminal-character", terminalCharacter.mn(secondQuoteSymbol));
    /** 4.16. */
    static final EBNF terminalString = REG.rule("terminal-string", firstQuoteSymbol.pl(firstTerminalCharacter.ix(),firstQuoteSymbol).or(
            secondQuoteSymbol.pl(secondTerminalCharacter.ix(),secondQuoteSymbol)));
    
    static final EBNF metaIdentifierCharacter = REG.rule("meta-identifier-character", letter.or(decimalDigit));

    public static final EBNF metaIdentifier = REG.rule("meta-identifier", letter.pl(metaIdentifierCharacter.x()));
    /** 4.13. */
    static final EBNF groupedSequence = REG.rule("grouped-sequence", startGroupSymbol.pl(REG.ref("definitions-list"), endGroupSymbol));
    /** 4.12. */
    static final EBNF repeatedSequence = REG.rule("repeated-sequence", startRepeatSymbol.pl(REG.ref("definitions-list"), endRepeatSymbol));
    /** 4.11. */
    static final EBNF optionalSequence = REG.rule("optional-sequence",startOptionSymbol.pl(REG.ref("definitions-list"), endOptionSymbol));
    /** 4.10. */
    static final EBNF syntacticPrimary = REG.rule("syntactic-primary", optionalSequence.or(repeatedSequence, groupedSequence, metaIdentifier,
            terminalString, specialSequence, emptySequence));
    /** 4.9. */
    static final BNF integer = REG.rule("integer",ABNF5234.DIGIT);
    static final BNF syntacticFactor = REG.rule("syntactic-factor", integer.pl(repetitionSymbol, syntacticPrimary).or(syntacticPrimary));
    static final BNF syntacticException = REG.rule("syntactic-exception", syntacticFactor);
    /** 4.6. */
    static final BNF syntacticTerm = REG.rule("syntactic-term", syntacticFactor.pl(exceptSymbol.pl(syntacticException).c()));
    /** 4.5. plus */
    static final BNF singleDefinition = REG.rule("single-definition", syntacticTerm.pl(concatenateSymbol).x().pl(syntacticTerm));
    /** 4.4. or select */
    static final BNF definitionsList = REG.rule("definitions-list",singleDefinition.pl(definitionSeparatorSymbol).x().pl(singleDefinition));
    static final EBNF syntaxRule = REG.rule("syntax-rule", metaIdentifier.pl(definingSymbol, definitionsList, terminatorSymbol));
    static final EBNF syntax = REG.rule("syntax", syntaxRule.ix());

}

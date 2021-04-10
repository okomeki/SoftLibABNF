package net.siisise.abnf.parser;

import net.siisise.abnf.ABNF;
import net.siisise.bnf.parser.BNFParser;

/**
 * ABNFの構文解析装置
 * @param <T>
 */
public interface ABNFParser<T> extends BNFParser<T> {

    @Override
    ABNF getBNF();
}

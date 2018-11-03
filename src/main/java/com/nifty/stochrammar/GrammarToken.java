// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar;

import java.util.Random;

/**
 * Encapsulates a single token of a context-free grammar. Tokens may be replaced by other tokens. Each type of token may
 * define the means of its own replacement via GrammarToken::replace.
 *
 * @param <T> extends Grammarable the type of object which this stochastic grammar can produce.
 */
public interface GrammarToken<T> {
    /**
     * A grammar token which returns an empty array.
     */
    GrammarToken EMPTY = new GrammarToken() {
        @Override
        public GrammarToken[] replace(Random rand) {
            return new GrammarToken[0];
        }
    };

    /**
     * Replaces this GrammarToken with one or more others. Repeated invocations applied to the GrammarTokens that result
     * from this method shall eventually produce a GroundToken with probability 1 as the number of such invocations
     * tends to infinity. Failure to understand and follow this contract will result in infinite loops for which the
     * implementer is entirely responsible.
     *
     * @param rand Random random number generator to use.
     * @return an array of non-null GrammarTokens, or null if there are no more replacements available for this token.
     */
    GrammarToken<T>[] replace(Random rand);
}

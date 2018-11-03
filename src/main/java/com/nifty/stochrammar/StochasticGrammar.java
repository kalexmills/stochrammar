// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar;

/**
 * Encapsulates the root production rule of a context-free stochastic grammar.
 *
 * @param <T> the type of object which this StochasticGrammar can produce.
 */
public interface StochasticGrammar<T> {
    /**
     * Generates a new root token for this grammar.
     * @return
     */
    GrammarToken<T> generateRootToken();

    /**
     * Generates a "blank" instance of the object to be acted on.
     * @return a newly constructed "blank" instance of object T
     */
    T blank();
}

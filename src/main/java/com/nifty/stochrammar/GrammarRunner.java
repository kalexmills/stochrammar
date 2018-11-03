// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar;

import java.util.Random;

/**
 * GrammmarRunner provides an algorithm for running a StochasticGrammar to generate objects of type T.
 *
 * @param <T>
 */
public abstract class GrammarRunner<T> {
    protected StochasticGrammar<T> grammar;

    public GrammarRunner(StochasticGrammar<T> grammar) {
        this.grammar = grammar;
    }
    /**
     * run() generates a new object of type T. The
     * @return
     */
    public T run() {
        return run(new Random());
    }

    /**
     * run(Random) generates a new object of type T using the Random instance passed in.
     * @param rand
     * @return
     */
    public abstract T run(Random rand);
}

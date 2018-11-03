// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar.runner;

import com.nifty.stochrammar.StochasticGrammar;

import java.util.Random;

/**
 * GrammmarRunner provides an algorithm for running a StochasticGrammar to generate objects of type T.
 *
 * @param <T> type of object this GrammarRunner builds.
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
     * run(Random) generates a new object of type T using the Random instance passed in. The implementation of this
     * method defines the order in which the GrammarToken::act methods of the StochasticGrammar passed in will get
     * called on the blank object(s) that are produced by the grammar.
     *
     * @param rand Random the RNG to use during the run.
     * @return T a generated object
     */
    public abstract T run(Random rand);
}

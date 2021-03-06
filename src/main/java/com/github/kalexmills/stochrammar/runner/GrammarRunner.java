/**
 * Copyright (c) 2018, NiftySoft LLC.
 *
 * This file is part of Stochrammar.
 *
 * Stochrammar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stochrammar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Stochrammar.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.kalexmills.stochrammar.runner;

import com.github.kalexmills.stochrammar.StochasticGrammar;

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
     * method defines the order in which the CFToken::act methods of the StochasticGrammar passed in will get
     * called on the blankEntity object(s) that are produced by the grammar.
     *
     * @param rand Random the RNG to use during the run.
     * @return T a generated object
     */
    public abstract T run(Random rand);
}

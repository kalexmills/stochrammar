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
package com.nifty.stochrammar;

import java.util.Random;

/**
 * Encapsulates a single token of a context-free grammar. Tokens may generate other tokens. Each type of token may
 * define the means of its own replacement via GrammarToken::replace.
 *
 * @param <T> extends Grammarable the type of object which this stochastic grammar can produce.
 */
public interface GrammarToken<T> {
    /**
     * EMPTY is a grammar token which returns an empty array. Clients should save some memory by using this instead of
     * constructing their own empty array.
     */
    GrammarToken[] EMPTY = new GrammarToken[0];

    /**
     * DEFAULT_RAND is a default random number generator used by clients who don't care enough to construct their own.
     */
    Random DEFAULT_RAND = new Random();

    default GrammarToken<T>[] replace() {
        return replace(DEFAULT_RAND);
    }

    /**
     * Replaces this GrammarToken with one or more others. Repeated invocations applied to the GrammarTokens that result
     * from this method shall eventually produce a GroundToken with probability 1 as the number of such invocations
     * tends to infinity. Failure to understand and follow this contract will result in infinite loops for which the
     * implementer is entirely responsible.
     *
     * @param rand Random random number generator to use.
     * @return an array of non-null GrammarTokens, or an empty array if this GrammarToken is a GroundToken.
     */
    GrammarToken<T>[] replace(Random rand);

    /**
     * Acts independently on an object of type T, possibly modifying its state. The same argument is returned to enable
     * chaining (if desired).
     *
     * By default, the act method is a no-op.
     *
     * @param object T object on which this token acts.
     * @return T the same argument which was passed in.
     */
    default T act(T object) { return object; };
}

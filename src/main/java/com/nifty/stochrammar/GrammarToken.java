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

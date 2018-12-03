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
package com.github.kalexmills.stochrammar;

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
    CFToken<T> generateRootToken();

    /**
     * Generates a "blankEntity" instance of the object to be acted on.
     * @return a newly constructed "blankEntity" instance of object T
     */
    T blankEntity();
}

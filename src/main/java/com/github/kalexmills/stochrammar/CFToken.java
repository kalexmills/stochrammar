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

import java.util.Random;

/**
 * CFToken represents a single token of a context-free grammar. Tokens know how to replace themselves Each token
 * defines the means of its own replacement via CFToken::replace.
 *
 * @param <T> the type of object which this stochastic grammar can produce.
 */
public abstract class CFToken<T> implements Token<T> {
    /**
     * EMPTY is a grammar token which returns an empty array. Clients should save some memory by using this instead of
     * constructing their own empty array.
     */
    public static final CFToken[] EMPTY = new CFToken[0];

    /**
     * DEFAULT_RAND is a default random number generator used by clients who don't care enough to construct their own.
     */
    public static final Random DEFAULT_RAND = new Random();

    private TokenAction<T> action;

    public CFToken() {
    }

    public CFToken(TokenAction<T> action) {
        this.action = action;
    }

    public CFToken<T>[] replace() {
        return replace(DEFAULT_RAND);
    }

    /**
     * Replaces this CFToken with one or more others. Repeated invocations of replace recursively applied to the
     * GrammarTokens that result from this method shall eventually produce a GroundToken with probability 1 as the
     * number of such invocations tends to infinity. Failure to understand and follow this contract will result in
     * infinite loops for which the implementer is entirely responsible.
     *
     * @param rand Random random number generator to use.
     * @return an array of non-null GrammarTokens, or an empty array if this CFToken is a GroundToken.
     */
    public abstract CFToken<T>[] replace(Random rand);

    /**
     *
     * @param action T object on which this token acts.
     */
    public void setAction(TokenAction<T> action) {
        this.action = action;
    }

    public final T act(T entity) {
        if (this.action == null) return entity;

        return this.action.act(entity);
    }
}

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
 * A GroundToken can no longer be replaced, and provides an act method which acts on an object of type T.
 */
public abstract class GroundToken<T> implements GrammarToken<T> {

    /**
     * The implementation for GroundTokens return null to indicate that they cannot be replaced.
     */
    @Override
    public GrammarToken<T>[] replace(Random rand) {
        return null;
    }

    /**
     * Acts independently on an object of type T, possibly modifying its state. The same argument is returned to enable
     * chaining (if desired).
     *
     * @param object T object on which this token acts.
     * @return T the same argument which was passed in.
     */
    public abstract T act(T object);
}

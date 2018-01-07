// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar;

import java.util.Random;

/**
 * A GroundToken can no longer be replaced, and provides an act method which acts on an object of type T.
 */
@IgnoreContext
public abstract class GroundToken<T> implements GrammarToken<T> {

    /**
     * The implementation for GroundTokens returns null to indicate that they cannot be replaced, and should be
     * cast to GroundTokens.
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

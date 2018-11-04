package com.nifty.stochrammar;

/**
 * TokenAction is a functional interface which defines the type of action a token may take.
 * @param <T>
 */
public interface TokenAction<T> {

    T act(T entity);
}

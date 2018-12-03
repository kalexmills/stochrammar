package com.github.kalexmills.stochrammar;

/**
 * Token acts on objects of type T
 */
public interface Token<T> {
    void setAction(TokenAction<T> action);
}

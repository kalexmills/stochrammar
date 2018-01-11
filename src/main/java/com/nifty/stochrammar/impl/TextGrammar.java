// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar.impl;

import com.nifty.stochrammar.GrammarToken;
import com.nifty.stochrammar.GroundToken;
import com.nifty.stochrammar.StochasticGrammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A grammar which returns plaintext. Contains a list of production rules, each of which is associated with a string
 * key.
 */
public class TextGrammar implements StochasticGrammar<StringBuilder>, GrammarToken<StringBuilder> {

    public static final String ROOT_KEY = "ROOT";

    private HashMap<String, ArrayList<BaseToken[]>> replaceMap;

    public TextGrammar() {
        replaceMap = new HashMap<>();
    }

    public void addRule(String key, BaseToken... tokens) {
        if(!replaceMap.containsKey(key)) replaceMap.put(key, new ArrayList<BaseToken[]>());
        replaceMap.get(key).add(tokens);
    }

    @Override
    public GrammarToken<StringBuilder>[] replace(Random rand) {
        if(!replaceMap.containsKey(ROOT_KEY)) return new GrammarToken[0];
        ArrayList<BaseToken[]> roots = replaceMap.get(ROOT_KEY);
        return roots.get(rand.nextInt(roots.size()));
    }

    @Override
    public GrammarToken<StringBuilder> generateRootToken() {
        return this;
    }

    @Override
    public StringBuilder blank() {
        return new StringBuilder();
    }

    /**
     * Used only to provide a base from which to inherit.
     */
    public interface BaseToken extends GrammarToken<StringBuilder> {}

    /**
     * A token which containts only a string literal.
     */
    public class StringToken extends GroundToken<StringBuilder> implements BaseToken {
        String text;

        public StringToken(String text) {
            this.text = text;
        }
        public StringBuilder act(StringBuilder str) {
            return str.append(text);
        }
    }

    /**
     * A token which contains only a non-literal to be replaced. Accepts a key
     */
    public class ReplaceToken implements BaseToken {
        String key;

        /**
         * @param key String the key for the replacement rule
         */
        public ReplaceToken(String key) {
            this.key = key;
        }

        /**
         * @param rand Random random number generator to use.
         * @return an array of grammar tokens to append to the list being grown.
         * @throws ReplaceException if the grammar does not contain the key by the time it is needed.
         */
        @Override
        public GrammarToken<StringBuilder>[] replace(Random rand) {
            if (replaceMap == null) throw new IllegalStateException();
            if (!replaceMap.containsKey(key)) throw new ReplaceException("Grammar does not contain key " + key);

            ArrayList<BaseToken[]> tokens = replaceMap.get(key);
            return tokens.get(rand.nextInt(tokens.size()));
        }
    }

    public static class ReplaceException extends IllegalStateException {
        public ReplaceException(String reason) {
            super(reason);
        }
    }
}

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
package com.github.kalexmills.stochrammar.impl;

import com.github.kalexmills.stochrammar.CFToken;
import com.github.kalexmills.stochrammar.GroundToken;
import com.github.kalexmills.stochrammar.StochasticGrammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A generic grammar which returns plaintext in the form of StringBuilders. Clients can add ProductionRules, each of
 * which is associated with a unique string key. The key "ROOT" is reserved for the root production rule.
 *
 * TODO: provide convenient Builder class.
 */
public class TextGrammar extends CFToken<StringBuilder> implements StochasticGrammar<StringBuilder> {

    public static final String ROOT_KEY = "ROOT";

    private HashMap<String, ArrayList<CFToken<StringBuilder>[]>> replaceMap;

    public TextGrammar() {
        replaceMap = new HashMap<>();
    }

    /**
     * addRule adds a ProductionRule to this TextGrammar.
     *
     * @param key String key associated with the LHS of the ProductionRule added
     * @param tokens BaseToken... list of BaseTokens associated with the RHS of the ProductionRule added.
     */
    public void addRule(String key, CFToken<StringBuilder>... tokens) {
        if(!replaceMap.containsKey(key)) replaceMap.put(key, new ArrayList<>());
        replaceMap.get(key).add(tokens);
    }

    @Override
    public CFToken<StringBuilder>[] replace(Random rand) {
        if(!replaceMap.containsKey(ROOT_KEY)) return CFToken.EMPTY;
        ArrayList<CFToken<StringBuilder>[]> roots = replaceMap.get(ROOT_KEY);
        return roots.get(rand.nextInt(roots.size()));
    }

    @Override
    public CFToken<StringBuilder> generateRootToken() {
        return this;
    }

    @Override
    public StringBuilder blankEntity() {
        return new StringBuilder();
    }

    /**
     * Literal is a GroundToken which contains only a string literal.
     */
    public class Literal extends GroundToken<StringBuilder> {
        String text;

        public Literal(String text) {
            this.text = text;
            this.setAction((StringBuilder str) -> str.append(text));
        }
    }

    /**
     * ProductionRule is a BaseToken uniquely identified by a string Key, which is replaced by other BaseToken
     * instances.
     */
    public class ProductionRule extends CFToken<StringBuilder> {
        String key;

        /**
         * @param key String the key for the replacement rule
         */
        public ProductionRule(String key) {
            this.key = key;
        }

        /**
         * @param rand Random random number generator to use.
         * @return an array of grammar tokens to append to the list being grown.
         * @throws ReplaceException if the grammar does not contain the key by the time it is needed.
         */
        @Override
        public CFToken<StringBuilder>[] replace(Random rand) {
            if (replaceMap == null) throw new IllegalStateException();
            if (!replaceMap.containsKey(key)) throw new ReplaceException("Grammar does not contain key " + key);

            ArrayList<CFToken<StringBuilder>[]> tokens = replaceMap.get(key);
            return tokens.get(rand.nextInt(tokens.size()));
        }
    }

    /**
     * ReplaceException is thrown when a replacement refers to the key of a non-existent ProductionRule.
     */
    public static class ReplaceException extends IllegalStateException {
        public ReplaceException(String reason) {
            super(reason);
        }
    }
}

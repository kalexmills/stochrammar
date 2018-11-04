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
package com.nifty.stochrammar.impl;

import com.nifty.stochrammar.runner.GroundSequenceRunner;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * TextGrammarTest implements a test of the TextGrammar implementation. The tests in this class are stochastic, so they
 * should fail only occasionally.
 *
 * TODO: Make tests deterministic by setting the random seed.
 */
public class TextGrammarTest {

    // A simple regex pattern for matching.
    Pattern ABRACADABRA = Pattern.compile("(abra|cadabra)+");

    @Test
    public void testRecursion() {
        // Create some simple regular expressions, and ensure that out of 100 samples, at least one of them is
        // recursive. This test is stochastic, and may occasionally fail.
        TextGrammar g = new TextGrammar();

        g.addRule(g.ROOT_KEY, g.new Literal("abra"));
        g.addRule(g.ROOT_KEY, g.new Literal("cadabra"));
        g.addRule(g.ROOT_KEY, g.new Literal("abra"), g.new ProductionRule(g.ROOT_KEY));
        g.addRule(g.ROOT_KEY, g.new Literal("cadabra"), g.new ProductionRule(g.ROOT_KEY));

        GroundSequenceRunner<StringBuilder> runner = new GroundSequenceRunner<>(g);

        int n = 100;
        int maxLength = 0;
        for (int i = 0; i < n; i++) {
            StringBuilder str = runner.run();
            maxLength = Math.max(str.length(), maxLength);
        }
        assertThat(maxLength >= "abracadabra".length(), is(true));
    }

    @Test
    public void testRegex() {
        // Create some simple regular expressions and test membership of 100 samples using a regex match.
        TextGrammar g = new TextGrammar();

        g.addRule(g.ROOT_KEY, g.new Literal("abra"));
        g.addRule(g.ROOT_KEY, g.new Literal("cadabra"));
        g.addRule(g.ROOT_KEY, g.new Literal("abra"), g.new ProductionRule(g.ROOT_KEY));
        g.addRule(g.ROOT_KEY, g.new Literal("cadabra"), g.new ProductionRule(g.ROOT_KEY));

        GroundSequenceRunner<StringBuilder> runner = new GroundSequenceRunner<>(g);

        int n = 100;
        for (int i = 0; i < n; i++) {
            StringBuilder str = runner.run();
            assertThat(ABRACADABRA.matcher(str).matches(), is(true));
        }
    }

    @Test
    public void testEmptyRoot() {
        // A grammar with an empty root should always generate the empty string, even if it contains other rules.
        TextGrammar g = new TextGrammar();
        g.addRule("A", g.new Literal("abra"),
                           g.new Literal("cadabra"));

        GroundSequenceRunner<StringBuilder> runner = new GroundSequenceRunner<>(g);

        assertThat(runner.run().toString(), is(equalTo("")));

        // However, when a path from the root to another rule exists, the root should take it.

        g.addRule(g.ROOT_KEY, g.new ProductionRule("A"));

        assertThat(runner.run().toString(), is(equalTo("abracadabra")));
    }
}

package com.nifty.stochrammar.impl;

import com.nifty.stochrammar.GrammarRunner;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class TextGrammarTest {

    // A simple regex pattern for matching.
    Pattern ABRACADABRA = Pattern.compile("(abra|cadabra)+");

    @Test
    public void recursionTest() {
        // Create some simple regular expressions ensure that out of 100 samples, at least one of them is recursive.
        TextGrammar g = new TextGrammar();

        g.addRule(g.ROOT_KEY, g.new StringToken("abra"));
        g.addRule(g.ROOT_KEY, g.new StringToken("cadabra"));
        g.addRule(g.ROOT_KEY, g.new StringToken("abra"), g.new ReplaceToken(g.ROOT_KEY));
        g.addRule(g.ROOT_KEY, g.new StringToken("cadabra"), g.new ReplaceToken(g.ROOT_KEY));

        GrammarRunner<StringBuilder> runner = new GrammarRunner<>(g);

        int n = 100;
        int maxLength = 0;
        for (int i = 0; i < n; i++) {
            StringBuilder str = runner.run();
            maxLength = Math.max(str.length(), maxLength);
        }
        assertThat(maxLength >= "abracadabra".length(), is(true));
    }

    @Test
    public void regexTest() {
        // Create some simple regular expressions and test membership of 100 samples using a regex match.
        TextGrammar g = new TextGrammar();

        g.addRule(g.ROOT_KEY, g.new StringToken("abra"));
        g.addRule(g.ROOT_KEY, g.new StringToken("cadabra"));
        g.addRule(g.ROOT_KEY, g.new StringToken("abra"), g.new ReplaceToken(g.ROOT_KEY));
        g.addRule(g.ROOT_KEY, g.new StringToken("cadabra"), g.new ReplaceToken(g.ROOT_KEY));

        GrammarRunner<StringBuilder> runner = new GrammarRunner<>(g);

        int n = 100;
        for (int i = 0; i < n; i++) {
            StringBuilder str = runner.run();
            assertThat(ABRACADABRA.matcher(str).matches(), is(true));
        }
    }

    @Test
    public void noRootTest() {
        // A grammar with an empty root should always generate the empty string, even if it contains other rules.
        TextGrammar g = new TextGrammar();
        g.addRule("A", g.new StringToken("abra"),
                           g.new StringToken("cadabra"));

        GrammarRunner<StringBuilder> runner = new GrammarRunner<>(g);

        assertThat(runner.run().toString(), is(equalTo("")));

        // However, when a path from the root to another rule exists, the root should take it.

        g.addRule(g.ROOT_KEY, g.new ReplaceToken("A"));

        assertThat(runner.run().toString(), is(equalTo("abracadabra")));
    }
}

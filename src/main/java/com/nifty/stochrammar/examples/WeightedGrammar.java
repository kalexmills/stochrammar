// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar.examples;

import com.nifty.stochrammar.GrammarToken;
import com.nifty.stochrammar.GroundToken;
import com.nifty.stochrammar.SequenceRunner;
import com.nifty.stochrammar.StochasticGrammar;

import java.util.Random;

/**
 * WeightedGrammar provides an example implementation that demonstrates how random weighting can be implemented by
 * client code. The grammar implemented follows the below weighted BNF grammar, in which the RHS of each production
 * rule is labeled with its probability of occurrence.
 *
 * ROOT := AB(0.2) | A(0.2) | ROOT(0.6)
 * A := "a"(0.5) | "A"(0.5)
 * B := "b"(0.5) | "B"(0.5)
 */
public class ExampleGrammar implements StochasticGrammar<String> {

    public ExampleGrammar() {

    }

    @Override
    public GrammarToken<String> generateRootToken() {
        return new RootToken();
    }

    @Override
    public String blank() {
        return new String();
    }

    private static class RootToken implements GrammarToken<String> {
        @Override
        public GrammarToken<String>[] replace(Random rand) {
            float sample = rand.nextFloat();
            if (sample < 0.2f) return new GrammarToken[] { new A(), new B() };
            if (sample < 0.4f) return new GrammarToken[] { new A() };
                               return new GrammarToken[] { new RootToken() };
        }
    }

    private static class A implements GrammarToken<String> {
        public GrammarToken<String>[] replace(Random rand) {
            float sample = rand.nextFloat();
            if (sample < 0.5f) return new GrammarToken[] { new Base("a")};
                               return new GrammarToken[] { new Base("A")};
        }
    }

    private static class B implements GrammarToken<String> {
        public GrammarToken<String>[] replace(Random rand) {
            float sample = rand.nextFloat();
            if (sample < 0.5f) return new GrammarToken[] { new Base("b")};
                               return new GrammarToken[] { new Base("B")};
        }
    }

    private static class Base extends GroundToken<String> {
        private String token;
        public Base(String token) {
            this.token = token;
        }
        public String act(String str) {
            return str + token;
        }
    }

    public static void main(String[] args) {
        SequenceRunner<String> runner = new SequenceRunner<>(new ExampleGrammar());

        int n = 100;
        for (int i = 0; i < n; ++i) {
            System.out.println(runner.run());
        }
    }
}
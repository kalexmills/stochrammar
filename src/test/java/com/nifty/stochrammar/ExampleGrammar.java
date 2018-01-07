// Copyright (c) 2018, NiftySoft LLC. All Rights Reserved.
package com.nifty.stochrammar;

import java.util.Random;

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
        GrammarRunner<String> runner = new GrammarRunner<>(new ExampleGrammar());

        int n = 100;
        for (int i = 0; i < n; ++i) {
            System.out.println(runner.run());
        }
    }
}
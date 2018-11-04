package com.nifty.stochrammar.runner;

import com.nifty.stochrammar.GrammarToken;
import com.nifty.stochrammar.GroundToken;
import com.nifty.stochrammar.StochasticGrammar;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeRunnerTest {

    /**
     * A convenient Ground class used by multiple tests.
     */
    private static class Ground extends GroundToken<String> {
        String val;
        public Ground(String val) { this.val = val;}
        public String act(String str) { return str + val; }
    }

    @Test
    public void testReplacesGrammarTokensWithGrounds() {
        class TokenA implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                return new GrammarToken[] { new Ground("b") };
            }
        }
        /**
         * A -> B
         * B -> "b"
         */
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        TreeRunner<String> underTest = new TreeRunner<>(new Grammar());

        assertThat(underTest.run()).isEqualTo("b");
    }

    @Test
    public void testChainReplacement() {
        /**
         * A -> B
         * B -> C
         * C -> "abc chain replaced with ground"
         */
        class TokenC implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                return new GrammarToken[] { new Ground("abc chain replaced with ground") };
            }
        }
        class TokenB implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                return new GrammarToken[]{new TokenC()};
            }
        }
        class TokenA implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[] { new TokenB() }; }
        }
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        TreeRunner<String> underTest = new TreeRunner<>(new Grammar());

        assertThat(underTest.run()).isEqualTo("abc chain replaced with ground");
    }

    @Test
    public void testBranchingProductionRule() {
        class TokenC implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                return new GrammarToken[] { new Ground("world") };
            }
        }
        class TokenB implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[]{new Ground("hello ")}; }
        }
        class TokenA implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[] { new TokenB(), new TokenC() }; }
        }
        /**
         * A -> BC
         * B -> "hello "
         * C -> "world"
         */
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        TreeRunner<String> underTest = new TreeRunner<>(new Grammar());
        underTest.setTraversalType(TreeRunner.TraversalType.BREADTH_FIRST);

        assertThat(underTest.run()).isEqualTo("hello world");
    }

    @Test
    public void testInvokesActOnNonGroundTokens() {
        class TokenA implements GrammarToken<String> {
            public String act(String str) { return "A"; }
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[] { new Ground("B") }; }
        }
        /**
         * A -> "AB"
         */
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        TreeRunner<String> underTest = new TreeRunner<>(new Grammar());

        assertThat(underTest.run()).isEqualTo("AB");
    }

    /**
     * A -> aBCa
     * B -> bDEb
     * C -> c
     * D -> d
     * E -> e
     *
     * Thus A -> abdebca
     *
     * A post-order traversal of the recursion tree would yield acbedba and a level-order traversal would yield
     * aabbcde, so this test's assertion implicitly excludes those outcomes.
     */
    class TraversalTestGrammar implements StochasticGrammar<String> {

        class TokenE implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                return new GrammarToken[] { new Ground("e") };
            }
        }
        class TokenD implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                return new GrammarToken[] { new Ground("d") };
            }
        }
        class TokenC implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[] { new Ground("c") }; }
        }
        class TokenB implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[]{ new Ground("b"), new TokenD(), new TokenE(), new Ground("b")}; }
        }
        class TokenA implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[] { new Ground("a"), new TokenB(), new TokenC(), new Ground("a") }; }
        }
        public GrammarToken<String> generateRootToken() {
            return new TokenA();
        }

        public String blankEntity() {
            return "";
        }
    }

    @Test
    public void testDepthFirstTraversal() {
        TreeRunner<String> underTest = new TreeRunner<>(new TraversalTestGrammar());
        underTest.setTraversalType(TreeRunner.TraversalType.DEPTH_FIRST);

        assertThat(underTest.run()).isEqualTo("acbedba");
    }

    @Test
    public void testBreadthFirstTraversal() {
        TreeRunner<String> underTest = new TreeRunner<>(new TraversalTestGrammar());
        underTest.setTraversalType(TreeRunner.TraversalType.BREADTH_FIRST);

        assertThat(underTest.run()).isEqualTo("aabbcde");
    }
}

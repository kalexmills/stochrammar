package com.nifty.stochrammar.runner;

import com.nifty.stochrammar.GrammarToken;
import com.nifty.stochrammar.GroundToken;
import com.nifty.stochrammar.StochasticGrammar;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class GroundSequenceRunnerTest {

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

        GroundSequenceRunner<String> underTest = new GroundSequenceRunner<>(new Grammar());

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

        GroundSequenceRunner<String> underTest = new GroundSequenceRunner<>(new Grammar());

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

        GroundSequenceRunner<String> underTest = new GroundSequenceRunner<>(new Grammar());

        assertThat(underTest.run()).isEqualTo("hello world");
    }

    @Test
    public void testDoesNotInvokeActOnNonGroundTokens() {
        class TokenA implements GrammarToken<String> {
            public String act(String str) { return "TEST FAILED"; }
            public GrammarToken<String>[] replace(Random rand) { return new GrammarToken[] { new Ground("TEST PASSED") }; }
        }
        /**
         * A -> "TEST PASSED"
         */
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        GroundSequenceRunner<String> underTest = new GroundSequenceRunner<>(new Grammar());

        assertThat(underTest.run()).isEqualTo("TEST PASSED");
    }

    @Test
    public void testInvokesActOnGroundsAccordingToPreOrderTraversal() {
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
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        GroundSequenceRunner<String> underTest = new GroundSequenceRunner<>(new Grammar());

        assertThat(underTest.run()).isEqualTo("abdebca");
    }

    @Test
    public void testDynamicArrayResizing() {
        class TokenA implements GrammarToken<String> {
            public GrammarToken<String>[] replace(Random rand) {
                // Check two rounds of doubling in the same iteration.
                GrammarToken[] result = new GrammarToken[GroundSequenceRunner.DEFAULT_BUFFER_SIZE * 4];
                for (int i = 0; i < result.length; ++i) {
                    result[i] = new Ground("a");
                }
                return result;
            }
        }
        class Grammar implements StochasticGrammar<String> {
            public GrammarToken<String> generateRootToken() {
                return new TokenA();
            }

            public String blankEntity() {
                return "";
            }
        }

        GroundSequenceRunner<String> underTest = new GroundSequenceRunner<>(new Grammar());

        String entity = underTest.run();
        assertThat(entity.length()).isEqualTo(GroundSequenceRunner.DEFAULT_BUFFER_SIZE*4);
        assertThat(entity).matches("^a+$");
    }
}

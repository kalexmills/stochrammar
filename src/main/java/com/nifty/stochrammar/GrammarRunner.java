package com.nifty.stochrammar;

import java.util.Arrays;
import java.util.Random;

/**
 * Encapsulates an algorithm for running a StochasticGrammar.
 */
public class GrammarRunner<T> {
    StochasticGrammar<T> grammar;

    private static final int DEFAULT_BUFFER_SIZE = 32;

    // Null-terminated buffers used to collect tokens as they are created.
    // NOTE: A more space-efficient implementation would probably use one set of buffers per thread.
    private GrammarToken[] tokenBuffer;
    private GrammarToken[] backBuffer;

    /**
     * Constructs a new GrammarRunner using the StochasticGrammar passed in as an argument.
     * @param grammar
     */
    public GrammarRunner(StochasticGrammar<T> grammar) {
        this.grammar = grammar;
        tokenBuffer = new GrammarToken[DEFAULT_BUFFER_SIZE];
        backBuffer = new GrammarToken[DEFAULT_BUFFER_SIZE];
    }

    public T run() {
        return run(new Random());
    }

    /**
     * Stochastically generates a new object of type T using the Random instance passed.
     *
     * @param rand Random a pre-seeded random number generator to be used for the generation.
     * @return T an instance of type T which results from the generation.
     */
    // While a faster implementation might do some extra work to avoid calls to known GroundTokens, this will
    // only yield a practical speedup in the presence of a very large number of tokens.
    public T run(Random rand) {
        tokenBuffer[0] = grammar.generateRootToken();
        tokenBuffer[1] = null;

        boolean tokensChanged = true;
        while (tokensChanged) {
            tokensChanged = false;

            int i = 0; // i points to indices in tokenBuffer
            int j = 0; // j points to indices in backBuffer
            while(i < tokenBuffer.length && tokenBuffer[i] != null) {
                GrammarToken[] tokens = tokenBuffer[i++].replace(rand);
                if(tokens != null) {
                    tokensChanged = true;
                    for (int k = 0; k < tokens.length; k++) {
                        backBuffer[j++] = tokens[k];
                        if(j > backBuffer.length) extendBackBuffer();
                    }
                    backBuffer[j] = null; // Ensure back buffer is null-terminated.
                }
            }
            if(tokensChanged) {
                // Swap front and back buffers.
                GrammarToken[] temp = tokenBuffer;
                tokenBuffer = backBuffer;
                backBuffer = temp;
            }
        }

        T result = grammar.blank();
        int i = 0;
        while (tokenBuffer[i] != null) {
            result = ((GroundToken<T>)tokenBuffer[i++]).act(result);
        }
        return result;
    }

    // Doubles the size of the back buffer. This change eventually propagates to the front buffer.
    private void extendBackBuffer() {
        backBuffer = Arrays.copyOf(backBuffer, backBuffer.length * 2);
    }
}
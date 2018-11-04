package com.nifty.stochrammar;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GrammarTokenTest {
    @Test
    public void testEmptyToken() {
        assertThat(GrammarToken.EMPTY.replace()).hasSize(0);
    }
}

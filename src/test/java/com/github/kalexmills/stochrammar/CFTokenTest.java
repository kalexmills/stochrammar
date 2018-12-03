package com.github.kalexmills.stochrammar;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CFTokenTest {
    @Test
    public void testEmptyToken() {
        assertThat(CFToken.EMPTY).hasSize(0);
    }
}

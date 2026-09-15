package com.htv.patterns.creational.singleton.basic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicSingletonTest {
    @Test
    void shouldAlwaysReturnSameInstance() {
        BasicSingleton first = BasicSingleton.getInstance();

        BasicSingleton second = BasicSingleton.getInstance();

        assertThat(first).isSameAs(second);
    }

    @Test
    void shouldExposeExpectedBehavior() {
        BasicSingleton basicSingleton = BasicSingleton.getInstance();

        assertThat(basicSingleton.description()).isEqualTo("Basic singleton pattern");
    }
}

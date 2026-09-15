package com.htv.patterns.creational.singleton.eager;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EagerSingletonTest {

    @Test
    void shouldReturnSameInstance() {
        EagerSingleton first = EagerSingleton.getInstance();

        EagerSingleton second = EagerSingleton.getInstance();

        assertThat(first).isSameAs(second);
    }

    @Test
    void shouldConstructorOnlyInstance() {
        EagerSingleton.getInstance();
        EagerSingleton.getInstance();
        EagerSingleton.getInstance();

        assertThat(EagerSingleton.constructorCount()).isEqualTo(1);
    }

    @Test
    void shouldKeepSameCreationTimestamp() {
        long firstTimestamp = EagerSingleton.getInstance().getCreateAt();

        long secondTimestamp = EagerSingleton.getInstance().getCreateAt();

        assertThat(firstTimestamp).isEqualTo(secondTimestamp);
    }
}

package com.htv.patterns.creational.singleton.lazy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LazySingletonTest {
    @AfterEach
    void cleanUp() {
        LazySingleton.resetForTest();
    }

    @Test
    void shouldNotCreateInstanceBeforeFirstAccess() {
        assertThat(LazySingleton.constructorCount()).isZero();
    }

    @Test
    void shouldCreateInstanceOnFirstAccess() {
        LazySingleton singleton = LazySingleton.getInstance();

        assertThat(singleton).isNotNull();

        assertThat(LazySingleton.constructorCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnSameInstanceSequentially() {
        LazySingleton first = LazySingleton.getInstance();
        LazySingleton second = LazySingleton.getInstance();

        assertThat(first).isSameAs(second);

        assertThat(LazySingleton.constructorCount()).isEqualTo(1);
    }

    @Test
    void shouldKeepSameCreationTimestamp() {
        long firstTimestamp = LazySingleton.getInstance().getCreateAt();
        long secondTimestamp = LazySingleton.getInstance().getCreateAt();

        assertThat(firstTimestamp).isEqualTo(secondTimestamp);
    }
}

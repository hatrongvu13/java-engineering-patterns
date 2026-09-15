package com.htv.patterns.creational.singleton.synchronizedsingleton;

import com.htv.patterns.creational.singleton.support.ConcurrentSingletonVerifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SynchronizedSingletonTest {
    @AfterEach
    void cleanUp() {
        SynchronizedSingleton.resetForTest();
    }

    @Test
    void shouldCreateInstanceLazily() {
        assertThat(SynchronizedSingleton.constructionCount()).isZero();
        SynchronizedSingleton.getInstance();
        assertThat(SynchronizedSingleton.constructionCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnSameInstanceSequentially() {
        SynchronizedSingleton first = SynchronizedSingleton.getInstance();
        SynchronizedSingleton second = SynchronizedSingleton.getInstance();
        assertThat(first).isSameAs(second);
    }

    @Test
    @Timeout(
            value = 10,
            unit = TimeUnit.SECONDS
    )
    void shouldReturnOneInstanceToConcurrentCallers() {
        Set<SynchronizedSingleton> singletons = ConcurrentSingletonVerifier.collectInstances(SynchronizedSingleton::getInstance, 64);
        assertThat(singletons.size()).isEqualTo(1);
        assertThat(SynchronizedSingleton.constructionCount()).isEqualTo(1);
    }
}

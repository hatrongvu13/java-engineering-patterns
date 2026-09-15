package com.htv.patterns.creational.singleton.doublechecked;

import com.htv.patterns.creational.singleton.support.ConcurrentSingletonVerifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

public class DoubleCheckedSingletonTest {
    @AfterEach
    void cleanUp() {
        DoubleCheckedSingleton.resetForTest();
    }

    @Test
    void shouldCreateInstanceLazily() {
        assertThat(DoubleCheckedSingleton.constructionCount()).isZero();
        DoubleCheckedSingleton.getInstance();
        assertThat(DoubleCheckedSingleton.constructionCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnSameInstanceSequentially() {
        DoubleCheckedSingleton first = DoubleCheckedSingleton.getInstance();
        DoubleCheckedSingleton second = DoubleCheckedSingleton.getInstance();
        assertThat(first).isSameAs(second);
    }

    @Test
    void shouldPublishFullyInitializedInstance() {
        DoubleCheckedSingleton singleton = DoubleCheckedSingleton.getInstance();
        assertThat(singleton.getConfiguration()).isEqualTo("default");
        assertThat(singleton.getCreateAt()).isPositive();
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void shouldReturnOneInstanceToConcurrentCallers() {
        Set<DoubleCheckedSingleton> instances = ConcurrentSingletonVerifier.collectInstances(DoubleCheckedSingleton::getInstance, 64);
        assertThat(instances.size()).isEqualTo(1);
        assertThat(DoubleCheckedSingleton.constructionCount()).isEqualTo(1);
    }
}

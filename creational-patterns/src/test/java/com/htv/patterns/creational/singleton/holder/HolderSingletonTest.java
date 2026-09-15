package com.htv.patterns.creational.singleton.holder;

import com.htv.patterns.creational.singleton.support.ConcurrentSingletonVerifier;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

public class HolderSingletonTest {
    @Test
    void shouldReturnSameInstanceSequentially() {
        HolderSingleton first = HolderSingleton.getInstance();
        HolderSingleton second = HolderSingleton.getInstance();
        assertThat(first).isSameAs(second);
    }

    @Test
    void shouldConstructOnlyInstance() {
        HolderSingleton.getInstance();
        HolderSingleton.getInstance();
        HolderSingleton.getInstance();
        assertThat(HolderSingleton.constructionCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnOneInstanceToConcurrentCallers() {
        Set<HolderSingleton> instances = ConcurrentSingletonVerifier.collectInstances(HolderSingleton::getInstance, 64);
        assertThat(instances.size()).isEqualTo(1);
        assertThat(HolderSingleton.constructionCount()).isEqualTo(1);
    }
}

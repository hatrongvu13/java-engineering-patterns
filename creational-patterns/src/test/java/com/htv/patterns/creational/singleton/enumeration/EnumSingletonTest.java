package com.htv.patterns.creational.singleton.enumeration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

public class EnumSingletonTest {
    @AfterEach
    void cleanUp() {
        EnumerationSingleton.INSTANCE.resetForTest();
    }

    @Test
    void shouldExposeOneEnumConstant() {
        EnumerationSingleton[] values = EnumerationSingleton.values();
        assertThat(values).containsExactly(EnumerationSingleton.INSTANCE);
    }

    @Test
    void shouldKeepStateOnSameInstance() {
        EnumerationSingleton singleton = EnumerationSingleton.INSTANCE;
        singleton.execute();
        singleton.execute();
        assertThat(EnumerationSingleton.INSTANCE.executionCount()).isEqualTo(2);
    }

    @Test
    void shouldPreserveIdentityAfterSerialization() throws IOException {
        EnumerationSingleton original = EnumerationSingleton.INSTANCE;
        byte[] serialized;
        EnumerationSingleton deserialized;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);
        try {
            output.writeObject(original);
            serialized = bytes.toByteArray();
        } finally {
            output.flush();
        }
        ByteArrayInputStream byteInput = new ByteArrayInputStream(serialized);
        ObjectInputStream input = new ObjectInputStream(byteInput);
        try {
            deserialized = (EnumerationSingleton) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        assertThat(deserialized).isSameAs(original);
    }
}

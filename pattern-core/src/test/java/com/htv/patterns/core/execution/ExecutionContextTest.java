package com.htv.patterns.core.execution;

import com.htv.patterns.core.metadata.PatternCategory;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExecutionContextTest {

    private static final Instant FIXED =
            Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void shouldBuildWithFixedClock() {
        ExecutionContext context =
                ExecutionContext.builder(
                                "Adapter",
                                PatternCategory.STRUCTURAL
                        )
                        .clock(Clock.fixed(FIXED, ZoneOffset.UTC))
                        .attribute("owner", "htv")
                        .build();

        assertThat(context.patternName()).isEqualTo("Adapter");
        assertThat(context.category())
                .isEqualTo(PatternCategory.STRUCTURAL);
        assertThat(context.startedAt()).isEqualTo(FIXED);
        assertThat(context.attribute("owner")).contains("htv");
    }

    @Test
    void shouldExposeImmutableAttributes() {
        ExecutionContext context =
                ExecutionContext.builder(
                                "Proxy",
                                PatternCategory.STRUCTURAL
                        )
                        .attribute("k", "v")
                        .build();

        assertThatThrownBy(
                () -> context.attributes().put("x", "y")
        )
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldRejectBlankPatternName() {
        assertThatThrownBy(
                () -> ExecutionContext.builder(
                        "  ",
                        PatternCategory.STRUCTURAL
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("patternName must not be blank");
    }

    @Test
    void shouldReturnEmptyForUnknownAttribute() {
        ExecutionContext context =
                ExecutionContext.builder(
                        "Facade",
                        PatternCategory.STRUCTURAL
                ).build();

        assertThat(context.attribute("missing")).isEmpty();
    }

    @Test
    void shouldHonourEqualityOnValue() {
        Clock clock = Clock.fixed(FIXED, ZoneOffset.UTC);

        ExecutionContext a =
                ExecutionContext.builder(
                        "Bridge",
                        PatternCategory.STRUCTURAL
                ).clock(clock).build();

        ExecutionContext b =
                ExecutionContext.builder(
                        "Bridge",
                        PatternCategory.STRUCTURAL
                ).clock(clock).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}

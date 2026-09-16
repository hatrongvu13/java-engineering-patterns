package com.htv.patterns.core.execution;

import com.htv.patterns.core.metadata.PatternCategory;
import com.htv.patterns.core.metadata.PatternStatus;
import com.htv.patterns.core.result.Result;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExecutionReportTest {

    private final ExecutionContext context =
            ExecutionContext.builder(
                    "Decorator",
                    PatternCategory.STRUCTURAL
            ).build();

    @Test
    void shouldReportCompletedFromSuccess() {
        ExecutionReport report =
                ExecutionReport.from(
                        context,
                        Result.success("ok"),
                        Duration.ofMillis(5)
                );

        assertThat(report.isCompleted()).isTrue();
        assertThat(report.status())
                .isEqualTo(PatternStatus.COMPLETED);
        assertThat(report.elapsed())
                .isEqualTo(Duration.ofMillis(5));
    }

    @Test
    void shouldReportExperimentalFromFailure() {
        ExecutionReport report =
                ExecutionReport.from(
                        context,
                        Result.failure("boom"),
                        Duration.ofMillis(2)
                );

        assertThat(report.isCompleted()).isFalse();
        assertThat(report.detailIfPresent()).contains("boom");
    }

    @Test
    void shouldRejectNegativeElapsed() {
        assertThatThrownBy(
                () -> new ExecutionReport(
                        context,
                        PatternStatus.COMPLETED,
                        Duration.ofMillis(-1),
                        "x"
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("elapsed must not be negative");
    }

    @Test
    void shouldNormaliseNullDetailToEmpty() {
        ExecutionReport report =
                new ExecutionReport(
                        context,
                        PatternStatus.COMPLETED,
                        Duration.ZERO,
                        null
                );

        assertThat(report.detail()).isEmpty();
        assertThat(report.detailIfPresent()).isEmpty();
    }
}

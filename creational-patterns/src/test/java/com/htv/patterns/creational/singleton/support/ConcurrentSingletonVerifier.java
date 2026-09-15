package com.htv.patterns.creational.singleton.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public final class ConcurrentSingletonVerifier {

    private static final int DEFAULT_THREAD_COUNT = 32;
    private static final long TIMEOUT_SECONDS = 5L;

    private ConcurrentSingletonVerifier() {
        throw new AssertionError(
                "Utility class must not be instantiated"
        );
    }

    public static <T> Set<T> collectInstances(
            Supplier<T> instanceSupplier
    ) {
        return collectInstances(
                instanceSupplier,
                DEFAULT_THREAD_COUNT
        );
    }

    public static <T> Set<T> collectInstances(
            Supplier<T> instanceSupplier,
            int threadCount
    ) {
        Objects.requireNonNull(
                instanceSupplier,
                "instanceSupplier must not be null"
        );

        if (threadCount <= 0) {
            throw new IllegalArgumentException(
                    "threadCount must be positive"
            );
        }

        ExecutorService executorService =
                Executors.newFixedThreadPool(threadCount);

        CountDownLatch ready =
                new CountDownLatch(threadCount);

        CountDownLatch start =
                new CountDownLatch(1);

        try {
            Callable<T> task = () -> {
                ready.countDown();

                /*
                 * Chờ thread test gọi start.countDown().
                 * Không dùng start.wait().
                 */
                boolean started = start.await(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

                if (!started) {
                    throw new IllegalStateException(
                            "Start signal was not received in time"
                    );
                }

                return instanceSupplier.get();
            };

            List<Future<T>> futures =
                    new ArrayList<>(threadCount);

            for (
                    int index = 0;
                    index < threadCount;
                    index++
            ) {
                futures.add(
                        executorService.submit(task)
                );
            }

            boolean allWorkersReady = ready.await(
                    TIMEOUT_SECONDS,
                    TimeUnit.SECONDS
            );

            if (!allWorkersReady) {
                throw new IllegalStateException(
                        "Workers did not become ready in time"
                );
            }

            /*
             * Giải phóng tất cả worker gần như cùng lúc.
             */
            start.countDown();

            Set<T> instances =
                    new HashSet<>();

            for (Future<T> future : futures) {
                T instance = future.get(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

                instances.add(instance);
            }

            return instances;
        } catch (InterruptedException exception) {
            /*
             * Khôi phục trạng thái interrupted trước khi
             * chuyển exception lên tầng gọi.
             */
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Concurrent singleton verification was interrupted",
                    exception
            );
        } catch (ExecutionException exception) {
            /*
             * Lấy nguyên nhân thật từ worker thread để lỗi
             * test dễ đọc hơn.
             */
            Throwable cause = exception.getCause();

            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            throw new IllegalStateException(
                    "A worker failed while obtaining the singleton",
                    cause
            );
        } catch (TimeoutException exception) {
            throw new IllegalStateException(
                    "Timed out while waiting for a singleton instance",
                    exception
            );
        } finally {
            /*
             * Quan trọng: nếu lỗi xảy ra trước start.countDown(),
             * giải phóng worker để chúng không bị kẹt.
             */
            start.countDown();

            executorService.shutdownNow();

            try {
                if (!executorService.awaitTermination(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                )) {
                    throw new IllegalStateException(
                            "Executor did not terminate in time"
                    );
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
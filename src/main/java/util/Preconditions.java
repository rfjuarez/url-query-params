package util;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Preconditions {
    public static <T> T nonNullArgument(T argument, String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message on Precondition.nonNullArgument cannot be null.");
        }
        if (argument == null) {
            throw new IllegalArgumentException(message);
        }
        return argument;
    }

    public static <T> T nonNullArgument(T argument) {
        if (argument == null) {
            throw new IllegalArgumentException();
        }
        return argument;
    }

    public static void check(final Supplier<Boolean> predicate, final String message) {
        nonNullArgument(predicate, "Predicate cannot be null");
        nonNullArgument(message, "Message cannot be null");

        if (!predicate.get()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check if the argument is not null or empty.
     * Supported types are: String, Object[], Collection, Map and byte[].
     *
     * @param argument Value to be checked
     * @param message  Message to be used in the exception
     * @param <T>     Type of the argument
     * @return The argument if it is not null or empty
     */
    public static <T> T nonNullOrEmptyArgument(T argument, String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message on Precondition.nonNullOrEmptyArgument cannot be null.");
        }
        if (argument == null) {
            throw new IllegalArgumentException(message);
        }
        if (argument instanceof String && ((String) argument).isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        if (argument instanceof Object[] && ((Object[]) argument).length == 0) {
            throw new IllegalArgumentException(message);
        }
        if (argument instanceof Collection && ((Collection<?>) argument).isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        if (argument instanceof byte[] && ((byte[]) argument).length == 0) {
            throw new IllegalArgumentException(message);
        }
        if (argument instanceof Map<?, ?> && ((Map<?, ?>) argument).isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        if (argument instanceof Map<?, ?>) {
            if (((Map<?, ?>) argument).values().stream().allMatch(Objects::isNull)) {
                throw new IllegalArgumentException(message);
            }
        }
        return argument;
    }
}

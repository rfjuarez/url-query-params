package util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PreconditionsTest {
    @Test
    void nonNullArgumentWithMessage() {
        String result = Preconditions.nonNullArgument("test", "message");
        assertEquals("test", result);
    }

    @Test
    void nonNullArgumentWithMessageNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullArgument("test", null),
                "Message on Precondition.nonNullArgument cannot be null."
        );
    }

    @Test
    void nonNullArgumentWithMessageAndNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullArgument(null, "test message"),
                "test message");
    }

    @Test
    void nonNullArgumentWithNullArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullArgument(null),
                "null");
    }

    @Test
    void nonNullArgumentArgumentWith() {
        String argument = "test";
        String result = Preconditions.nonNullArgument(argument);
        assertEquals("test", result);
    }

    @Test
    void nonNullArgumentOrEmptyWithMessage() {
        String result = Preconditions.nonNullOrEmptyArgument("test", "message");
        assertEquals("test", result);
    }

    @Test
    void nonNullOrEmptyArgumentWithMessageNullArgumentArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullOrEmptyArgument("test", null),
                "Message on Precondition.nonNullOrEmptyArgument cannot be null."
        );
    }

    @Test
    void nonNullOrEmptyArgumentWithMessageAndNullArgumentArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullOrEmptyArgument(null, "test message"),
                "test message");
    }

    @Test
    void nonNullArgumentOrEmptyArgumentWithEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullOrEmptyArgument("", "test message"),
                "test message"
        );
    }

    @Test
    void nonNullArgumentOrEmptyArgumentWithEmptyArrayOfBytes() {
        byte[] argument = new byte[0];
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullOrEmptyArgument(argument, "test message"),
                "test message"
        );
    }

    @Test
    void nonNullArgumentOrEmptyArgumentWithEmptyList() {
        List<String> argument = new ArrayList<>();
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.nonNullOrEmptyArgument(argument, "test message"),
                "test message"
        );
    }
}
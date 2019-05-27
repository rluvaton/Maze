package Helpers.ThrowableAssertions;

import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectAssertionTest {


    @ParameterizedTest(name = "{index}: ObjectAssertion.requireNonNull({0}, Direction.{1}) = {2}")
    @MethodSource("provideDataForRequireNonNull")
    void requireNonNull(boolean isNull, Object testedObject, String message) {
        Executable requireNonNullFn = () -> ObjectAssertion.requireNonNull(testedObject, message);

        if (isNull) {
            assertThrows(NullPointerException.class, requireNonNullFn, message);
        } else {
            assertDoesNotThrow(requireNonNullFn);
        }
    }

    private static Stream<Arguments> provideDataForRequireNonNull() {
        String noMessage = "";
        return Stream.of(
                Arguments.of(
                        true,
                        null,
                        "Shouldn't be null"
                ),
                Arguments.of(
                        false,
                        true,
                        noMessage
                ),
                Arguments.of(
                        false,
                        false,
                        noMessage
                ),
                Arguments.of(
                        true,
                        null,
                        "Boolean Shouldn't be null"
                ),
                Arguments.of(
                        false,
                        1,
                        noMessage
                ),
                Arguments.of(
                        false,
                        0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Integer.MAX_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Integer.MIN_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        (short)1,
                        noMessage
                ),
                Arguments.of(
                        false,
                        (short)0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Short.MAX_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Short.MIN_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        1.0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        -1.0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        1L,
                        noMessage
                ),
                Arguments.of(
                        false,
                        -1L,
                        noMessage
                ),
                Arguments.of(
                        false,
                        0L,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Long.MAX_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Long.MIN_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        0x0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        0x1,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Double.MAX_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Double.MIN_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Double.MIN_NORMAL,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Float.MAX_VALUE,
                        noMessage
                ),
                Arguments.of(
                        false,
                        Float.MIN_NORMAL,
                        noMessage
                ),
                Arguments.of(
                        false,
                        (float)0.0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        (float)1.0,
                        noMessage
                ),
                Arguments.of(
                        false,
                        (float)-1.0,
                        noMessage
                )
        );
    }

}
package player;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MoveStatusTest {

    @ParameterizedTest(name = "{index}: getValidationMoveFromBoolean({0}) -> MoveStatus.{1}")
    @MethodSource("provideDataForMoveCoordinatesToDirectionWithCoordinates")
    void getValidationMoveFromBoolean(boolean isValid, MoveStatus expectedStatus) {
        assertNotNull(expectedStatus);
        assertEquals(expectedStatus, MoveStatus.getValidationMoveFromBoolean(isValid));
    }

    private static Stream<Arguments> provideDataForMoveCoordinatesToDirectionWithCoordinates() {
        return Stream.of(
                Arguments.of(true, MoveStatus.VALID),
                Arguments.of(false, MoveStatus.INVALID_MOVE)
        );
    }

}
package san.desafiolatam.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapacityTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void shouldRejectCapacityWhenValueIsNotPositive(int value) {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Capacity(value)
        );

        // Assert
        assertEquals(
                "Room capacity must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateCapacityWithPositiveValue() {
        // Act
        Capacity capacity = new Capacity(10);

        // Assert
        assertEquals(10, capacity.value());
    }
}

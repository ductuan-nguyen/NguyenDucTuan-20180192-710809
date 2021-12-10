import controller.PlaceRushOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateReceiveDateTest {
    // Nguyen Duc Tuan - 20180192
    private PlaceRushOrderController placeRushOrderController;
    @BeforeEach
    void setUp() {
        placeRushOrderController = new PlaceRushOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "2021-12-30, true",
            "2021-09-13, false",
            "2021-01-11, false",
            ", false"
    })
    void validateReceiveDate(String date, boolean expected){
        boolean result = placeRushOrderController.validateReceiveDate(date);
        assertEquals(expected, result);
    }
}

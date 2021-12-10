import controller.PlaceRushOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateProvinceTest {
    // Nguyen Duc Tuan - 20180192
    private PlaceRushOrderController placeRushOrderController;
    @BeforeEach
    void setUp() {
        placeRushOrderController = new PlaceRushOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "Hà Nội, true",
            "Ha Noi, false",
            "Ha Nam, false",
            ", false"
    })
    void validateProvince(String province, boolean expected){
        boolean result = placeRushOrderController.validateProvince(province);
        assertEquals(expected, result);
    }
}

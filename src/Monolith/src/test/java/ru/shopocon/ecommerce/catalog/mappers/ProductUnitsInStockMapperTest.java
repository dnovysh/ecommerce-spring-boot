package ru.shopocon.ecommerce.catalog.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UnitsInStock mapper should")
class ProductUnitsInStockMapperTest {

    private static final int LOW_STOCK_BOUNDARY = 5;

    @Test
    @DisplayName("return INSTOCK")
    void shouldReturnINSTOCK() {
        var productUnitsInStockMapper = new ProductUnitsInStockMapper(LOW_STOCK_BOUNDARY);
        String actual = productUnitsInStockMapper.mapToInventoryStatus(10);
        assertEquals("INSTOCK", actual);
    }

    @Test
    @DisplayName("return LOWSTOCK")
    void shouldReturnLOWSTOCK() {
        var productUnitsInStockMapper = new ProductUnitsInStockMapper(LOW_STOCK_BOUNDARY);
        String actual = productUnitsInStockMapper.mapToInventoryStatus(3);
        assertEquals("LOWSTOCK", actual);
    }

    @Test
    @DisplayName("return OUTOFSTOCK")
    void shouldReturnOUTOFSTOCK() {
        var productUnitsInStockMapper = new ProductUnitsInStockMapper(LOW_STOCK_BOUNDARY);
        String actual = productUnitsInStockMapper.mapToInventoryStatus(0);
        assertEquals("OUTOFSTOCK", actual);
    }
}

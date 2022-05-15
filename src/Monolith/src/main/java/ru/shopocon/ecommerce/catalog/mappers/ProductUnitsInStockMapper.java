package ru.shopocon.ecommerce.catalog.mappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.catalog.domain.types.InventoryStatus;

@SuppressWarnings("ClassCanBeRecord")
@Component()
public class ProductUnitsInStockMapper {

    private final int lowStockBoundary;

    public ProductUnitsInStockMapper(
        @Value("${shopocon.catalog.low-stock-boundary: 3}") int lowStockBoundary) {
        this.lowStockBoundary = lowStockBoundary;
    }

    public String mapToInventoryStatus(int unitsInStock) {
        if (unitsInStock > this.lowStockBoundary) {
            return InventoryStatus.INSTOCK.name();
        } else if (unitsInStock > 0) {
            return InventoryStatus.LOWSTOCK.name();
        }
        return InventoryStatus.OUTOFSTOCK.name();
    }
}

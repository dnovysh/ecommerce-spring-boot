package ru.shopocon.ecommerce.catalog.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.shopocon.ecommerce.catalog.domain.Category;
import ru.shopocon.ecommerce.catalog.domain.Product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Projection(name = "inlineCategory", types = {Product.class})
public interface InlineCategory {

    @Value("#{target.id}")
    Long getId();

    Long getDealerId();

    String getSku();

    Category getCategory();

    String getName();

    String getDescription();

    String getImage();

    boolean getActive();

    int getUnitsInStock();

    BigDecimal getUnitPrice();

    Integer getRating();

    @Value("#{@productUnitsInStockMapper.mapToInventoryStatus(target.getUnitsInStock())}")
    String getInventoryStatus();

    OffsetDateTime getDateCreated();

    OffsetDateTime getLastUpdated();
}

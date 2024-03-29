package ru.shopocon.ecommerce.catalog.domain;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@SuppressWarnings("com.haulmont.jpb.LombokEqualsAndHashCodeInspection")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product", schema = "ec_catalog",
    uniqueConstraints = {@UniqueConstraint(
        name = "un_product_dealer_sku", columnNames = {"dealer_id", "sku"}
    )}
)
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "dealer_id", nullable = false)
    private Long dealerId;

    @EqualsAndHashCode.Include
    @Column(name = "sku", nullable = false)
    private String sku;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 20)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(name = "units_in_stock", nullable = false)
    private int unitsInStock = 0;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "popularity_index")
    private int popularityIndex;

    @Column(name = "date_created", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime dateCreated;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime lastUpdated;
}

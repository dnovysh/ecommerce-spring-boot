package ru.shopocon.ecommerce.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_category", schema = "ec_catalog",
        uniqueConstraints = {@UniqueConstraint(
                name = "un_product_category_name", columnNames = {"category_name"}
        )}
)
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name;
}

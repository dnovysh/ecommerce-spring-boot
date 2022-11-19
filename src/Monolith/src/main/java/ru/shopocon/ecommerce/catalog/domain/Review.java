package ru.shopocon.ecommerce.catalog.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "product_review", schema = "ec_catalog")
public class Review {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_alias", nullable = false)
    private String userAlias;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "hidden")
    private boolean hidden;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "date_created", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime dateCreated;
}

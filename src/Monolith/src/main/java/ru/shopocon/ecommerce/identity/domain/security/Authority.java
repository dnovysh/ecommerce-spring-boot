package ru.shopocon.ecommerce.identity.domain.security;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Application metadata, adding, modifying, and deleting operations are prohibited
 */
@SuppressWarnings("com.haulmont.jpb.LombokEqualsAndHashCodeInspection")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@Entity
@Table(name = "authority", schema = "ec_identity")
public class Authority implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "permission", nullable = false)
    private String permission;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private transient Set<Role> roles;
}

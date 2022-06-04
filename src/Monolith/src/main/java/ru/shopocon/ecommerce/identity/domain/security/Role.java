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
@Table(name = "role", schema = "ec_identity")
public class Role implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private transient Set<User> users;

    @Singular
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_authority", schema = "ec_identity",
        joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities;
}

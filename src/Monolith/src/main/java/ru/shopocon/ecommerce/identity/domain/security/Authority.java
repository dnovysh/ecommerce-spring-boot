package ru.shopocon.ecommerce.identity.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "authority", schema = "ec_identity")
public class Authority {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "permission", nullable = false)
    private String permission;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}

package ru.shopocon.ecommerce.identity.domain;

import lombok.*;
import ru.shopocon.ecommerce.identity.domain.security.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "dealer", schema = "ec_identity")
public class Dealer implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "dealer", fetch = FetchType.LAZY)
    private transient Set<User> users;
}

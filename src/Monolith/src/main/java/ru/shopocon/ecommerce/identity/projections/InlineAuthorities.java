package ru.shopocon.ecommerce.identity.projections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.shopocon.ecommerce.identity.domain.security.Authority;
import ru.shopocon.ecommerce.identity.domain.security.Role;

import java.util.Set;

@Projection(name = "inlineAuthorities", types = {Role.class})
public interface InlineAuthorities {
    @Value("#{target.id}")
    Long getId();

    String getName();

    Set<Authority> getAuthorities();
}

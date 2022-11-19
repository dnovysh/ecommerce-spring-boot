package ru.shopocon.ecommerce.identity.mappers;

import ru.shopocon.ecommerce.identity.model.UserDetailsJwt;
import ru.shopocon.ecommerce.identity.model.UserDetailsJwtPlain;

public interface UserDetailsJwtMapper {
    UserDetailsJwtPlain mapToUserDetailsJwtPlain(UserDetailsJwt userDetailsJwt);

    UserDetailsJwt mapFromUserDetailsJwtPlain(UserDetailsJwtPlain userDetailsJwtPlain);
}

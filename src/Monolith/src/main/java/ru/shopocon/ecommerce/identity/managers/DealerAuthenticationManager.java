package ru.shopocon.ecommerce.identity.managers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.identity.domain.types.DealerRepresentative;

@Component
public class DealerAuthenticationManager {

    public boolean dealerIdMatches(Authentication authentication, Long dealerId) {
        if (authentication.getPrincipal() instanceof DealerRepresentative dealerRepresentative) {
            final Long IdOfDealerRepresentedByUser = dealerRepresentative.getIdOfDealerRepresentedByUser();
            return IdOfDealerRepresentedByUser != null && IdOfDealerRepresentedByUser.equals(dealerId);
        }
        return false;
    }

    public boolean isDealerRepresentative(Authentication authentication) {
        if (authentication.getPrincipal() instanceof DealerRepresentative dealerRepresentative) {
            return dealerRepresentative.getIdOfDealerRepresentedByUser() != null;
        }
        return false;
    }

    public Long getDealerId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof DealerRepresentative dealerRepresentative) {
            return dealerRepresentative.getIdOfDealerRepresentedByUser();
        }
        return null;
    }
}

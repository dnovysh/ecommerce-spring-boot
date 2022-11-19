package ru.shopocon.ecommerce.identity.mappers;

import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.identity.domain.Dealer;
import ru.shopocon.ecommerce.identity.model.DealerDto;

@Component
public class DealerMapper {
    DealerDto mapToDealerDto(Dealer dealer) {
        return new DealerDto(dealer.getId(), dealer.getName());
    }
}

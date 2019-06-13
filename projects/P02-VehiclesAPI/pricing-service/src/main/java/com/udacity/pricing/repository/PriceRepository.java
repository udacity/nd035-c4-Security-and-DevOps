package com.udacity.pricing.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.stereotype.Repository;
import com.udacity.pricing.service.dto.PriceResponse;

@Repository
public class PriceRepository {

    private static final Map<Long, PriceResponse> PRICES = LongStream
            .range(1, 20)
            .mapToObj(i -> new PriceResponse("USD", randomPrice(), i))
            .collect(Collectors.toMap(PriceResponse::getVehicleId, p -> p));

    private static BigDecimal randomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean contains(Long id) {
        return PRICES.containsKey(id);
    }

    public PriceResponse get(Long id) {
        return PRICES.get(id);
    }
}

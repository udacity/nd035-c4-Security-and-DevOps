package com.udacity.pricing.service;

import javax.jws.WebService;
import com.udacity.pricing.repository.PriceRepository;
import com.udacity.pricing.service.dto.PriceRequest;
import com.udacity.pricing.service.dto.PriceResponse;

@WebService
public class PricePortImpl implements PriceService {

    private final PriceRepository prices;

    public PricePortImpl(PriceRepository prices) {
        this.prices = prices;
    }

    @Override
    public PriceResponse getPrice(PriceRequest request) throws PriceException {

        if (!prices.contains(request.getVehicleId())) {
            throw new PriceException("Cannot find price for Vehicle " + request.getVehicleId());
        }

        return prices.get(request.getVehicleId());
    }


}

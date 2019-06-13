package com.udacity.pricing.service;

import com.udacity.pricing.service.dto.PriceRequest;
import com.udacity.pricing.service.dto.PriceResponse;

public interface PriceService {

    PriceResponse getPrice(PriceRequest request) throws PriceException;


}

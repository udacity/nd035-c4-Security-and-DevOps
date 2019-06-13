package com.udacity.vehicles.client.prices;

import com.udacity.pricing.service.PriceException_Exception;
import com.udacity.pricing.service.PricePortImpl;
import com.udacity.pricing.service.PriceRequest;
import com.udacity.pricing.service.PriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final PricePortImpl pricePort;

    public PriceClient(PricePortImpl pricePort) {
        this.pricePort = pricePort;
    }

    // In a real-world application we'll want
    // to add some resilience to this method
    // with retries/CB/failover capabilities
    // We may also want to cache the results
    // so we don't need to do a request every
    // time
    public String getPrice(Long vehicleId) {
        try {

            PriceRequest priceRequest = new PriceRequest();
            priceRequest.setVehicleId(vehicleId);
            PriceResponse priceResponse = pricePort.getPrice(priceRequest);

            return String.format("%s %s", priceResponse.getCurrency(), priceResponse.getPrice());

        } catch (PriceException_Exception e) {
            log.warn("Could not retrieve price for vehicle {}", vehicleId);
        } catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return "(consult price)";
    }
}

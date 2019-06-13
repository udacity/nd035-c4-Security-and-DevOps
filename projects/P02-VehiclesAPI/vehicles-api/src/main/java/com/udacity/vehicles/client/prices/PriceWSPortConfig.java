package com.udacity.vehicles.client.prices;

import com.udacity.pricing.service.PricePortImpl;
import com.udacity.pricing.service.PricePortImplService;
import javax.xml.ws.BindingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriceWSPortConfig {

    @Bean
    public PricePortImpl pricePort(@Value("${pricing.endpoint}") String pricingEndpoint) {
        PricePortImplService pricePortImplService =
                new PricePortImplService();

        PricePortImpl pricePort = pricePortImplService.getPricePortImplPort();

        BindingProvider bp = (BindingProvider) pricePort;
        bp.getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pricingEndpoint);

        return pricePort;
    }
}

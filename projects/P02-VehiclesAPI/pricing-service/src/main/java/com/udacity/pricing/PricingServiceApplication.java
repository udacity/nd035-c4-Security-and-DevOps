package com.udacity.pricing;

import com.udacity.pricing.repository.PriceRepository;
import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.udacity.pricing.service.PricePortImpl;

@SpringBootApplication
public class PricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

    @Bean
    public Endpoint endpoint(Bus bus, PriceRepository priceRepository) {
        EndpointImpl endpoint = new EndpointImpl(bus, new PricePortImpl(priceRepository));
        endpoint.publish("/price");
        return endpoint;
    }

}

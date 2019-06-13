package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final CarRepository repository;
    private final PriceClient priceClient;
    private final MapsClient maps;

    public CarService(CarRepository repository, PriceClient priceClient,
            MapsClient maps) {
        this.repository = repository;
        this.priceClient = priceClient;
        this.maps = maps;
    }

    public List<Car> list() {
        return repository.findAll();
    }

    public Car findById(Long id) {
        Optional<Car> optionalCar = repository.findById(id);
        Car car = optionalCar.orElseThrow(CarNotFoundException::new);
        String price = priceClient.getPrice(id);
        car.setPrice(price);

        Location withAddress = maps.getAddress(car.getLocation());
        car.setLocation(withAddress);

        return car;
    }

    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }
        return repository.save(car);
    }

    public void delete(Long id) {
        Optional<Car> optionalCar = repository.findById(id);
        Car carToDelete = optionalCar.orElseThrow(CarNotFoundException::new);
        repository.delete(carToDelete);
    }
}

package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.CarModel;
import pl.studia.InstaCar.repository.CarModelRepository;

import java.util.List;

@Service
public class CarModelService {
    private final CarModelRepository carModelRepository;

    @Autowired
    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public long count() {
        return carModelRepository.count();
    }

    public void saveAll(Iterable<CarModel> models) {
        carModelRepository.saveAll(models);
    }
}

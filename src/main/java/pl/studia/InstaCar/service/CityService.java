package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.City;
import pl.studia.InstaCar.repository.CityRepository;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public long count() {
        return cityRepository.count();
    }

    @CacheEvict(value = "allCities", allEntries = true)
    public List<City> saveAll(Iterable<City> cities) {
        return cityRepository.saveAll(cities);
    }

    @Caching(
            put = {
                    @CachePut(value = "cities", key = "#result.id"),
                    @CachePut(value = "cities", key = "#result.name")
            },
            evict = @CacheEvict(value = "allCities", allEntries = true)
    )
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Cacheable(value = "cities", key = "#name")
    public City getCityByName(String name) {
        return cityRepository.findByName(name).orElseThrow(
                () -> new NoSuchElementException("City not found with name: " + name)
        );
    }

    @Cacheable(value = "cities", key = "#id")
    public City getCityById(Long id) {
        return cityRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("City not found with id: " + id)
        );
    }

    @Cacheable(value = "allCities")
    public List<City> getAllCities() {
        List<City> cities = cityRepository.findAll();
        cities.sort(Comparator.comparing(City::getName));
        return cities;
    }
}

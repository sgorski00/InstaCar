package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.repository.UserDetailsRepository;

import java.util.NoSuchElementException;

@Service
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserDetailsService(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @CacheEvict(value = "userDetails", allEntries = true)
    public void saveAll(Iterable<UserDetails> userDetails) {
        userDetailsRepository.saveAll(userDetails);
    }

    @CachePut(value = "userDetails", key = "#result.id")
    public UserDetails save(UserDetails userDetails) {
        return userDetailsRepository.findByUser(userDetails.getUser()).map(
                    ud -> {
                        ud.setAddress(userDetails.getAddress());
                        ud.setCity(userDetails.getCity());
                        ud.setFirstName(userDetails.getFirstName());
                        ud.setLastName(userDetails.getLastName());
                        ud.setPhoneNumber(userDetails.getPhoneNumber());
                        ud.setPostalCode(userDetails.getPostalCode());
                        return userDetailsRepository.save(ud);
                    }
        ).orElseGet(() -> userDetailsRepository.save(userDetails));
    }

    @Cacheable(value = "userDetails", key = "#id")
    public UserDetails getUserDetails(long id) {
        return userDetailsRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User details not found with id: " + id)
        );
    }

    @CacheEvict(value = "userDetails", key = "#id")
    public void deleteById(long id) {
        userDetailsRepository.deleteById(id);
    }
}

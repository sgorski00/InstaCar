package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.UserDetails;
import pl.studia.InstaCar.repository.UserDetailsRepository;

import java.util.NoSuchElementException;

@Service
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final MessageSource messageSource;

    @Autowired
    public UserDetailsService(UserDetailsRepository userDetailsRepository, @Qualifier("messageSource") MessageSource messageSource) {
        this.userDetailsRepository = userDetailsRepository;
        this.messageSource = messageSource;
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
                () -> {
                    String message = messageSource.getMessage("error.user.details.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message + ": " + id);
                }
        );
    }

    @CacheEvict(value = "userDetails", key = "#id")
    public void deleteById(long id) {
        userDetailsRepository.deleteById(id);
    }
}

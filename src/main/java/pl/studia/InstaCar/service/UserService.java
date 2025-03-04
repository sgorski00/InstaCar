package pl.studia.InstaCar.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmpty() {
        return userRepository.count() == 0;
    }

    public void save(ApplicationUser user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                () -> new NoSuchElementException("Podano błędną nazwę użytkownika!")
        );
        log.info("User found: {}", username);
        if (!user.isEnabled()){
            log.error("User {} not enabled", username);
            throw new IllegalArgumentException("Konto nie jest aktywowane!");
        }
        return user;
    }
}

package pl.studia.InstaCar.service;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public void saveAll(List<ApplicationUser> users) {
        userRepository.saveAll(users);
    }

    /**
     * Finds user by username.
     *
     * @param username the username to search for
     * @return the user details of the user
     * @throws UsernameNotFoundException if user is not found, account not enabled or not local
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                () -> new UsernameNotFoundException("Podano błędną nazwę użytkownika!")
        );
        log.info("User found: {}", username);
        if (!user.isEnabled()){
            log.error("User {} not enabled", username);
            throw new UsernameNotFoundException("Konto nie jest aktywowane!");
        }

        if(user.getProvider().equals(AuthProvider.GOOGLE)) {
            throw new UsernameNotFoundException("Zaloguj się poprzez konto Google!");
        }
        return user;
    }

    public long count() {
        return userRepository.count();
    }

    /**
     * Find a user by their username or email address.
     * @param identifier either the username or the email address of the user to find
     * @return the user if found, otherwise throws a {@link UsernameNotFoundException}
     */
    public ApplicationUser findByUsernameOrEmail(String identifier) {
        Optional<ApplicationUser> user = userRepository.findByUsernameIgnoreCase(identifier);
        if(user.isPresent()) {
            return user.get();
        }
        user = userRepository.findByEmailIgnoreCase(identifier);
        return user.orElseThrow(
                () -> new UsernameNotFoundException("Nie odnaleziono użytkownika: " + identifier)
        );
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public List<ApplicationUser> findAll() {
        return userRepository.findAll();
    }

    public Page<ApplicationUser> findAllPaged(String query, Pageable pageable) {
        query = query == null || query.isBlank() ? null : "%" + query.toLowerCase() + "%";
        return userRepository.findAllBySearch(query, pageable);
    }

    public ApplicationUser findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono użytkownika o id: " + id)
        );
    }

    public ApplicationUser findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono użytkownika: " + username)
        );
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}

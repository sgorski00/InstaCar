package pl.studia.InstaCar.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.ApplicationUser;
import pl.studia.InstaCar.model.authentication.AuthProvider;
import pl.studia.InstaCar.model.authentication.CustomUserPrincipal;
import pl.studia.InstaCar.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Autowired
    public UserService(UserRepository userRepository, @Qualifier("messageSource") MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
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
                () -> {
                    String message = messageSource.getMessage("error.login.not.found", null, LocaleContextHolder.getLocale());
                    return new UsernameNotFoundException(message);
                }
        );
        log.info("User found: {}", username);
        if (!user.isEnabled()){
            log.error("User {} not enabled", username);
            String message = messageSource.getMessage("error.login.not.active", null, LocaleContextHolder.getLocale());
            throw new UsernameNotFoundException(message);
        }

        if(user.getProvider().equals(AuthProvider.GOOGLE)) {
            String message = messageSource.getMessage("error.login.not.local", null, LocaleContextHolder.getLocale());
            throw new UsernameNotFoundException(message);
        }
        return new CustomUserPrincipal(user);
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
                () -> {
                    String message = messageSource.getMessage("error.login.not.found", null, LocaleContextHolder.getLocale());
                    return new UsernameNotFoundException(message + ": " + identifier);
                }
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
                () -> {
                    String message = messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message +": " + id);
                }
        );
    }

    public ApplicationUser findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                () -> {
                    String message = messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale());
                    return new NoSuchElementException(message + ": " + username);
                }
        );
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}

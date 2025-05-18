package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.repository.RoleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    @Autowired
    public RoleService(RoleRepository roleRepository, @Qualifier("messageSource") MessageSource messageSource) {
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
    }

    public boolean isEmpty() {
        return roleRepository.count() == 0;
    }

    @Caching(
            put = {
                    @CachePut(value = "roles", key = "#role.name"),
                    @CachePut(value = "roles", key = "#result.id")
            }
    )
    public Role save(Role role) {
         return roleRepository.save(role);
    }

    @Cacheable(value = "roles", key = "#name")
    public Role findByName(String name) {
        String message = messageSource.getMessage("error.role.not.found", null, LocaleContextHolder.getLocale());
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new NoSuchElementException(message + ": " + name)
        );
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Cacheable(value = "roles", key = "#id")
    public Role findById(Long id) {
        String message = messageSource.getMessage("error.role.not.found", null, LocaleContextHolder.getLocale());
        return roleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(message + ": " + id)
        );
    }
}

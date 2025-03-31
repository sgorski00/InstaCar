package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.repository.RoleRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.NoSuchElementException;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean isEmpty() {
        return roleRepository.count() == 0;
    }

    @CachePut(value = "roles", key = "#role.name")
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Cacheable(value = "roles", key = "#name")
    public Role findByName(String name) {
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono roli: " + name)
        );
    }
}

package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.repository.RoleRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
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
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono roli: " + name)
        );
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Cacheable(value = "roles", key = "#id")
    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono roli z id: " + id)
        );
    }
}

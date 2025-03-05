package pl.studia.InstaCar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.studia.InstaCar.model.authentication.Role;
import pl.studia.InstaCar.repository.RoleRepository;

import java.util.Arrays;
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

    public void saveAll(Role... roles) {
        roleRepository.saveAll(Arrays.stream(roles).toList());
    }

    public Role findByName(String name) throws NoSuchElementException {
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new NoSuchElementException("Nie odnaleziono roli: " + name)
        );
    }
}

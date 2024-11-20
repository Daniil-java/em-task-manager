package com.em.taskmanager.services;

import com.em.taskmanager.entities.Role;
import com.em.taskmanager.entities.RoleName;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.ROLE_NOT_FOUND));
    }
}

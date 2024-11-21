package com.em.taskmanager.mappers;

import com.em.taskmanager.dtos.RoleDto;
import com.em.taskmanager.dtos.mappers.RoleMapper;
import com.em.taskmanager.entities.Role;
import com.em.taskmanager.entities.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RoleMapperTest {
    @Autowired
    private RoleMapper roleMapper ;

    @Test
    void testToDto() {
        Role role = new Role().setName(RoleName.ROLE_USER);
        RoleDto roleDto = roleMapper.toDto(role);

        assertNotNull(roleDto);
        assertEquals(role.getName().name(), roleDto.getName());
    }

    @Test
    void testToEntity() {
        RoleDto roleDto = new RoleDto().setName(RoleName.ROLE_USER.name());

        Role role = roleMapper.toEntity(roleDto);

        assertNotNull(role);
        assertEquals(roleDto.getName(), role.getName().name());
    }

    @Test
    void testToDtoList() {
        List<Role> roleList = Collections.singletonList(new Role());
        List<RoleDto> roleDtoList = roleMapper.toDtoList(roleList);

        assertNotNull(roleDtoList);
        assertEquals(roleList.size(), roleDtoList.size());
    }

    @Test
    void testToEntityList() {
        List<RoleDto> roleDtoList = Collections.singletonList(new RoleDto());
        List<Role> roleList = roleMapper.toEntityList(roleDtoList);

        assertNotNull(roleList);
        assertEquals(roleDtoList.size(), roleList.size());
    }
}

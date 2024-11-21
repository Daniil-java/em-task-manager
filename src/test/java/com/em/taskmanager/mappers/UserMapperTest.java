package com.em.taskmanager.mappers;

import com.em.taskmanager.dtos.UserDto;
import com.em.taskmanager.dtos.mappers.UserMapper;
import com.em.taskmanager.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testToDto() {
        User user = new User()
                .setId(1L)
                .setUsername("testUser");

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
    }

    @Test
    void testToEntity() {
        UserDto userDto = new UserDto()
                .setId(1L)
                .setUsername("testUser");

        User user = userMapper.toEntity(userDto);
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getUsername(), user.getUsername());
    }
}

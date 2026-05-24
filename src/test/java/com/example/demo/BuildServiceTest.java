package com.example.demo;

import com.example.demo.model.Build;
import com.example.demo.model.User;
import com.example.demo.repository.BuildRepository;
import com.example.demo.service.BuildServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuildServiceTest {

    @Mock
    private BuildRepository buildRepository;

    @InjectMocks
    private BuildServiceImpl buildService;

    @Test
    void createBuild() {
        User user = new User();
        user.setId(1L);
        user.setEmail("Тест_Юзер");

        Build build = new Build();
        build.setName("Тест билд");
        build.setDescription("Описание");
        build.setHelmet("Шлем");
        build.setArmor("Тест билд");
        build.setTrousers("Штаны");

        when(buildRepository.save(any(Build.class))).thenReturn(build);

        Build savedBuild = buildService.createBuild(build, user);

        assertNotNull(savedBuild, "Сохранённый билд не должен быть null");
        assertEquals("Тест билд", savedBuild.getName());
        assertEquals(user, savedBuild.getOwner());

        verify(buildRepository, times(1)).save(build);
    }
}
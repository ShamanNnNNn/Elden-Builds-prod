package com.example.demo;
import com.example.demo.controller.BuildController;
import com.example.demo.dto.BuildResponse;
import com.example.demo.model.Build;
import com.example.demo.model.User;
import com.example.demo.service.BuildService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildControllerTest {

    @Mock
    private BuildService buildService;

    @InjectMocks
    private BuildController buildController;

    @Test
    void getBuildById_ShouldReturnBuild_WhenBuildExists() {
        Long buildId = 1L;

        User owner = new User();
        owner.setId(10L);
        owner.setEmail("author@test.com");

        Build build = new Build();
        build.setId(buildId);
        build.setName("Test Build");
        build.setDescription("Test Description");
        build.setOwner(owner);

        when(buildService.getBuildById(buildId)).thenReturn(Optional.of(build));

        ResponseEntity<BuildResponse> response = buildController.getBuildById(buildId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(buildId, response.getBody().getId());
        assertEquals("Test Build", response.getBody().getName());
        assertEquals("Test Description", response.getBody().getDescription());

        verify(buildService).getBuildById(buildId);
    }

    @Test
    void getBuildById_ShouldThrowException_WhenBuildNotFound() {

        Long nonExistentId = 999L;

        when(buildService.getBuildById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            buildController.getBuildById(nonExistentId);
        });

        assertEquals("Build not found", exception.getMessage());
        verify(buildService).getBuildById(nonExistentId);
    }
}
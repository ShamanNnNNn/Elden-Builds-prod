package com.example.demo;

import com.example.demo.model.Build;
import com.example.demo.repository.BuildRepository;
import com.example.demo.service.BuildServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildNotFoundTest {

    @Mock
    private BuildRepository buildRepository;

    @InjectMocks
    private BuildServiceImpl buildService;

    @Test
    void notBuild() {
        Long nonExistentId = 1L;
        when(buildRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Build> result = buildService.getBuildById(nonExistentId);

        assertTrue(result.isEmpty());
        verify(buildRepository, times(1)).findById(nonExistentId);
    }
}

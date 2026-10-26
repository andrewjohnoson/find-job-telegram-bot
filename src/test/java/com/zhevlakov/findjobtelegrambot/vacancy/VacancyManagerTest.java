package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.provider.VacancyProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacancyManagerTest {
    @Mock
    private VacancyProvider remotiveVacancyProvider;
    @Mock
    private VacancyProvider arbeitnowVacancyProvider;
    @Mock
    private VacancyMapper vacancyMapper;
    @Mock
    private VacancySyncService vacancySyncService;

    private VacancyManager vacancyManager;

    @BeforeEach
    void setUp() {
        vacancyManager = new VacancyManager(
                vacancySyncService,
                vacancyMapper,
                List.of(remotiveVacancyProvider, arbeitnowVacancyProvider)
        );
    }

    @Test
    void shouldFetchInParallel_AndSyncBoth() {
        Long userId = 1L;
        when(remotiveVacancyProvider.fetch(userId)).
                thenAnswer(_ -> {
                    Thread.sleep(500);
                    return List.of(new VacancyDto(null, null, null, null, null, null, null));
                }
        );

        when(arbeitnowVacancyProvider.fetch(userId)).
                thenAnswer(_ -> {
                    Thread.sleep(500);
                    return List.of(new VacancyDto(null, null, null, null, null, null, null));
                });

        when(vacancyMapper.toDomain(anyList())).thenReturn(List.of(new Vacancy()));

        assertTimeout(Duration.ofMillis(800), () -> {
            vacancyManager.fetch(userId);
        });

        verify(vacancySyncService, times(2)).syncWithDatabase(eq(userId), anyList());
    }

    @Test
    void shouldNotFailWhenProviderReturnsException() {
        Long userId = 1L;
        when(remotiveVacancyProvider.fetch(userId)).thenThrow(new RuntimeException("Сайт упал."));
        when(arbeitnowVacancyProvider.fetch(userId)).thenAnswer(_ ->
                List.of(new VacancyDto(null, null, null, null, null, null, null)));

        when(vacancyMapper.toDomain(anyList())).thenReturn(List.of(new Vacancy()));

        assertDoesNotThrow(() -> vacancyManager.fetch(userId));

        verify(vacancySyncService, times(1)).syncWithDatabase(eq(userId), anyList());
    }

    @Test
    void shouldNotCallDatabase_WhenProviderReturnsEmptyList() {
        Long userId = 1L;

        when(remotiveVacancyProvider.fetch(userId)).thenReturn(Collections.emptyList());
        when(arbeitnowVacancyProvider.fetch(userId)).thenReturn(Collections.emptyList());

        vacancyManager.fetch(userId);

        verify(vacancyMapper, never()).toDomain(anyList());
        verify(vacancySyncService, never()).syncWithDatabase(eq(userId), anyList());
    }

}
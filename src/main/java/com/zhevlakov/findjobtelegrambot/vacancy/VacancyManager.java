package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.vacancy.provider.VacancyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Component
public class VacancyManager {
    private final Logger log = LoggerFactory.getLogger(VacancyManager.class);

    private final Semaphore rateLimiter = new Semaphore(30);

    // Сервисы
    private final VacancySyncService vacancySyncService;

    private final VacancyMapper vacancyMapper;
    private final List<VacancyProvider> providers;

    public VacancyManager(
            VacancySyncService vacancySyncService,
            VacancyMapper vacancyMapper,
            List<VacancyProvider> providers
    ) {
        this.vacancySyncService = vacancySyncService;
        this.vacancyMapper = vacancyMapper;
        this.providers = providers;
    }

    public void fetch(Long userId) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var provider : providers) {
                executor.submit(() -> {
                    try {
                        rateLimiter.acquire();
                        try {
                            List<VacancyDto> vacancyDtoList = provider.fetch(userId);
                            if (!vacancyDtoList.isEmpty()) {
                                List<Vacancy> vacancies = vacancyMapper.toDomain(vacancyDtoList);
                                vacancySyncService.syncWithDatabase(userId, vacancies);
                            }
                        } finally {
                            rateLimiter.release();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        log.error("Ошибка при сборе вакансий для userId={}", userId, e);
                    }
                });
            }

        }
    }
}

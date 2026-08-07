package com.zhevlakov.findjobtelegrambot.user.query;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {
    private final UserQueryRepository userQueryRepository;

    public UserQueryService(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    public UserQuery getById(Long chatId) {
        return userQueryRepository.findByUserEntity_ChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Нет элемента с chatId=" + chatId));
    }

    public UserQuery updateQuery(UserQuery query) {
        UserQuery newQuery = new UserQuery(
                query.getId(),
                query.getPosition(),
                query.getExperience(),
                query.getCity(),
                query.getWorkFormat(),
                query.getSalary(),
                query.getEmploymentType(),
                query.getUserEntity()
        );

        return userQueryRepository.save(newQuery);
    }
}

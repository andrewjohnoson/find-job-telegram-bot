package com.zhevlakov.findjobtelegrambot.user.query;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {
    private final UserQueryRepository userQueryRepository;

    public UserQueryService(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    public UserQuery getByChatId(Long chatId) {
        return userQueryRepository.findByUserEntity_ChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Нет элемента с chatId=" + chatId));
    }

    public boolean hasQuery(Long chatId) {
        return userQueryRepository.existsByUserEntity_ChatId(chatId);
    }

    public UserQuery updateQuery(UserQuery query) {
        return userQueryRepository.save(query);
    }
}

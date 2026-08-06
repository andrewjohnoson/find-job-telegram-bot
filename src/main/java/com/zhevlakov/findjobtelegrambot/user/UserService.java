package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public UserEntity createNewUser(Long chatId, String userTag) {
        if (userRepository.existsById(chatId)) {
            return getUserById(chatId);
        }

        UserEntity newUser = new UserEntity(chatId, userTag, FsmStates.FREE, null);
        return userRepository.save(newUser);
    }

    public UserEntity getUserById(Long chatId) {
        return userRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Нет элемента с chatId=" + chatId));
    }

    public boolean haveUser(Long chatId) {
        return userRepository.existsById(chatId);
    }

    public boolean isUserFree(Long chatId) {
        return userRepository.existsByChatIdAndState(chatId, FsmStates.FREE);
    }

    public UserEntity updateUser(UserEntity user) {
        UserEntity updatedUser = new UserEntity(
                user.getChatId(),
                user.getUserTag(),
                user.getState(),
                user.getUserRequest()
        );

        return userRepository.save(updatedUser);
    }
}

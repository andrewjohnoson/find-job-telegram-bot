package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_tag", nullable = false)
    private String userTag;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private FsmStates state;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private UserRequest userRequest;

    public UserEntity() {
    }

    public UserEntity(
            Long chatId,
            String userTag,
            FsmStates state,
            UserRequest userRequest
    ) {
        this.chatId = chatId;
        this.userTag = userTag;
        this.state = state;
        this.userRequest = userRequest;
    }

    public void setUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
        userRequest.setUserEntity(this);
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public void setState(FsmStates state) {
        this.state = state;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getUserTag() {
        return userTag;
    }

    public FsmStates getState() {
        return state;
    }



    public UserRequest getUserRequest() {
        return userRequest;
    }
}

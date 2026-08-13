package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
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
    private UserQue ry userQuery;

    public UserEntity() {
    }

    public UserEntity(
            Long chatId,
            String userTag,
            FsmStates state,
            UserQuery userQuery
    ) {
        this.chatId = chatId;
        this.userTag = userTag;
        this.state = state;
        this.userQuery = userQuery;
    }

    public void setUserRequest(UserQuery userQuery) {
        this.userQuery = userQuery;
        userQuery.setUserEntity(this);
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



    public UserQuery getUserRequest() {
        return userQuery;
    }
}

package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_tag", nullable = false)
    private String userTag;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private FsmStates state;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private UserRequest userRequest;

    public void setUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
        userRequest.setUserEntity(this);
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
}

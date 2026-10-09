package com.zhevlakov.findjobtelegrambot.user;

import com.zhevlakov.findjobtelegrambot.fsm.FsmStates;
import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;
import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_tag", nullable = false)
    private String userTag;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private FsmStates state;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private UserQuery userQuery;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserVacancy> userVacancyList = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(
            Long userId,
            String userTag,
            FsmStates state,
            UserQuery userQuery
    ) {
        this.userId = userId;
        this.userTag = userTag;
        this.state = state;
        this.userQuery = userQuery;
    }

    public void setUserRequest(UserQuery userQuery) {
        this.userQuery = userQuery;
        userQuery.setUserEntity(this);
    }

    public void addUserVacancy(UserVacancy userVacancy) {
        userVacancyList.add(userVacancy);
        userVacancy.setUser(this);
    }

    public void setUserId(Long chatId) {
        this.userId = chatId;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public void setState(FsmStates state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
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

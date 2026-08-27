package com.zhevlakov.findjobtelegrambot.user.vacancy;

import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.vacancy.Vacancy;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "user_vacancy")
public class UserVacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VacancyStatus status;

    public UserVacancy() {
    }

    public UserVacancy(
            Long id,
            UserEntity user,
            Vacancy vacancy,
            VacancyStatus status
    ) {
        this.id = id;
        this.user = user;
        this.vacancy = vacancy;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public VacancyStatus getStatus() {
        return status;
    }

    public void setStatus(VacancyStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if (vacancy != null) {
            if (status.equals(VacancyStatus.FAVOURITE)) {
                return "⭐" + vacancy;
            }

            if (status.equals(VacancyStatus.HIDDEN)) {
                return "\uD83E\uDEE3" + vacancy;
            }

            return vacancy.toString();
        }
        return null;
    }
}

package com.zhevlakov.findjobtelegrambot.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_request")
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position")
    private String position;

    @Column(name = "experience")
    private String experience;

    @Column(name = "city")
    private String city;

    @Column(name = "work_format")
    private String workFormat;

    @Column(name = "salary")
    private String salary;

    @Column(name = "employment_type")
    private String employmentType;

    @OneToOne()
    @JoinColumn(name = "user_chat_id", referencedColumnName = "chat_id")
    private UserEntity userEntity;

    public UserRequest(
            Long id,
            String position,
            String experience,
            String city,
            String workFormat,
            String salary,
            String employmentType,
            UserEntity userEntity
    ) {
        this.id = id;
        this.position = position;
        this.experience = experience;
        this.city = city;
        this.workFormat = workFormat;
        this.salary = salary;
        this.employmentType = employmentType;
        this.userEntity = userEntity;
    }
}

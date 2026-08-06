package com.zhevlakov.findjobtelegrambot.user;

import jakarta.persistence.*;

@Entity
@Table(name = "user_request")
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

    @OneToOne
    @JoinColumn(name = "user_chat_id", referencedColumnName = "chat_id")
    private UserEntity userEntity;

    public UserRequest() {
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setWorkFormat(String workFormat) {
        this.workFormat = workFormat;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getExperience() {
        return experience;
    }

    public String getCity() {
        return city;
    }

    public String getWorkFormat() {
        return workFormat;
    }

    public String getSalary() {
        return salary;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    @Override
    public String toString() {
        return """
                <i>Должность:</i> %s,
                <i>опыт:</i> %s,
                <i>город:</i> %s,
                <i>формат работы:</i> %s,
                <i>желаемая зарплата:</i> %s,
                <i>тип занятости:</i> %s.
                """.formatted(position, experience, city,
                                workFormat, salary, employmentType);
    }
}

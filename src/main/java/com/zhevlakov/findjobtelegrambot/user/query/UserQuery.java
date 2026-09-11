package com.zhevlakov.findjobtelegrambot.user.query;

import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "user_query")
public class UserQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position", nullable = false)
    private String position;

    @ElementCollection
    @CollectionTable(
            name = "user_query_experience_types",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "experience")
    private Set<String> experienceList = new HashSet<>();

    @Column(name = "city")
    private String city;

    @ElementCollection
    @CollectionTable(
            name = "user_query_work_format_type",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "work_format")
    private Set<String> workFormatList = new HashSet<>();

    @Column(name = "salary")
    private String salary;

    @ElementCollection
    @CollectionTable(
            name = "user_query_employment_type",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "employment_type")
    private Set<String> employmentTypeList = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_chat_id", referencedColumnName = "chat_id")
    private UserEntity userEntity;

    public UserQuery() {
    }

    public UserQuery(
            Long id,
            String position,
            String city,
            String salary,
            UserEntity userEntity
    ) {
        this.id = id;
        this.position = position;
        this.city = city;
        this.salary = salary;
        this.userEntity = userEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Set<String> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(Set<String> experienceList) {
        this.experienceList = experienceList;
    }

    public Set<String> getWorkFormatList() {
        return workFormatList;
    }

    public void setWorkFormatList(Set<String> workFormatList) {
        this.workFormatList = workFormatList;
    }

    public Set<String> getEmploymentTypeList() {
        return employmentTypeList;
    }

    public void setEmploymentTypeList(Set<String> employmentTypeList) {
        this.employmentTypeList = employmentTypeList;
    }

    public Long getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public String getCity() {
        return city;
    }

    public String getSalary() {
        return salary;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void addEmploymentType(String input) {
        employmentTypeList.add(input);
    }

    public void removeEmploymentType(String input) {
        employmentTypeList.remove(input);
    }

    public void addWorkFormat(String input) {
        workFormatList.add(input);
    }

    public void removeWorkFormat(String input) {
        workFormatList.remove(input);
    }

    public void addExperience(String input) {
        experienceList.add(input);
    }

    public void removeExperience(String input) {
        experienceList.remove(input);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",\n", "", ".");

        if (position != null)           sj.add("<i>Должность:</i> " + position);
        if (experienceList != null && !experienceList.isEmpty())     sj.add("<i>опыт:</i> " + experienceList);
        if (city != null)               sj.add("<i>город:</i> " + city);
        if (workFormatList != null && !workFormatList.isEmpty())     sj.add("<i>формат работы:</i> " + workFormatList);
        if (salary != null)             sj.add("<i>желаемая зарплата:</i> " + salary);
        if (employmentTypeList != null && !employmentTypeList.isEmpty()) sj.add("<i>тип занятости:</i> " + employmentTypeList);

        return sj.toString();
    }
}

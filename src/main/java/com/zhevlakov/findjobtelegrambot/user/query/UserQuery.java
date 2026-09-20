package com.zhevlakov.findjobtelegrambot.user.query;

import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.EmploymentType;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.Experience;
import com.zhevlakov.findjobtelegrambot.vacancy.query.converter.WorkFormat;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

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
    @Enumerated(EnumType.STRING)
    private Set<Experience> experienceList = new HashSet<>();

    @Column(name = "city")
    private String city;

    @ElementCollection
    @CollectionTable(
            name = "user_query_work_format_type",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "work_format")
    @Enumerated(EnumType.STRING)
    private Set<WorkFormat> workFormatList = new HashSet<>();

    @Column(name = "salary")
    private String salary;

    @ElementCollection
    @CollectionTable(
            name = "user_query_employment_type",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "employment_type")
    @Enumerated(EnumType.STRING)
    private Set<EmploymentType> employmentTypeList = new HashSet<>();

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

    public Set<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(Set<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public Set<WorkFormat> getWorkFormatList() {
        return workFormatList;
    }

    public void setWorkFormatList(Set<WorkFormat> workFormatList) {
        this.workFormatList = workFormatList;
    }

    public Set<EmploymentType> getEmploymentTypeList() {
        return employmentTypeList;
    }

    public void setEmploymentTypeList(Set<EmploymentType> employmentTypeList) {
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

    public void addEmploymentType(EmploymentType input) {
        employmentTypeList.add(input);
    }

    public void removeEmploymentType(EmploymentType input) {
        employmentTypeList.remove(input);
    }

    public void addWorkFormat(WorkFormat input) {
        workFormatList.add(input);
    }

    public void removeWorkFormat(WorkFormat input) {
        workFormatList.remove(input);
    }

    public void addExperience(Experience input) {
        experienceList.add(input);
    }

    public void removeExperience(Experience input) {
        experienceList.remove(input);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",\n", "", ".");

        if (position != null)           sj.add("<i>Должность:</i> " + position);
        if (experienceList != null && !experienceList.isEmpty()) {
            String expText = experienceList.stream()
                            .map(Experience::getUiText)
                            .collect(Collectors.joining(", "));
            sj.add("<i>опыт:</i> " + expText);
        }
        if (city != null)               sj.add("<i>город:</i> " + city);
        if (workFormatList != null && !workFormatList.isEmpty()) {
            String workFormatText = workFormatList.stream()
                            .map(WorkFormat::getUiText)
                            .collect(Collectors.joining(", "));
            sj.add("<i>формат работы:</i> " + workFormatText);
        }
        if (salary != null)             sj.add("<i>желаемая зарплата:</i> " + salary);
        if (employmentTypeList != null && !employmentTypeList.isEmpty()) {
            String employmentTypeText = employmentTypeList.stream()
                    .map(EmploymentType::getUiText)
                    .collect(Collectors.joining(", "));
            sj.add("<i>тип занятости:</i> " + employmentTypeText);
        }

        return sj.toString();
    }
}

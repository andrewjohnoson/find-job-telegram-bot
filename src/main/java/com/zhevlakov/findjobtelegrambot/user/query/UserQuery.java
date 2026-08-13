package com.zhevlakov.findjobtelegrambot.user.query;

import com.zhevlakov.findjobtelegrambot.user.UserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "user_query")
public class UserQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position")
    private String position;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_query_experience_types",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "experience")
    private List<String> experienceList = new ArrayList<>();

    @Column(name = "city")
    private String city;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_query_work_format_type",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "work_format")
    private List<String> workFormatList = new ArrayList<>();

    @Column(name = "salary")
    private String salary;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_query_employment_type",
            joinColumns = @JoinColumn(name = "user_query_id")
    )
    @Column(name = "employment_type")
    private List<String> employmentTypeList = new ArrayList<>();

    @OneToOne
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

    public List<String> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<String> experienceList) {
        this.experienceList = experienceList;
    }

    public List<String> getWorkFormatList() {
        return workFormatList;
    }

    public void setWorkFormatList(List<String> workFormatList) {
        this.workFormatList = workFormatList;
    }

    public List<String> getEmploymentTypeList() {
        return employmentTypeList;
    }

    public void setEmploymentTypeList(List<String> employmentTypeList) {
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
        if (!employmentTypeList.contains(input)) {
            employmentTypeList.add(input);
        }
    }

    public void removeEmploymentType(String input) {
        employmentTypeList.remove(input);
    }

    public void addWorkFormat(String input) {
        if (!workFormatList.contains(input)) {
            workFormatList.add(input);
        }
    }

    public void removeWorkFormat(String input) {
        workFormatList.remove(input);
    }

    public void addExperience(String input) {
        if (!experienceList.contains(input)) {
            experienceList.add(input);
        }
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

package com.zhevlakov.findjobtelegrambot.vacancy;

import com.zhevlakov.findjobtelegrambot.user.vacancy.UserVacancy;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "vacancy")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "city")
    private String city;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "experience")
    private String experience;

    @Column(name = "salary")
    private String salary;

    @Column(name = "work_format")
    private String workFormat;

    @Column(name = "url")
    private String url;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL)
    private List<UserVacancy> userVacancyList = new ArrayList<>();

    public Vacancy() {
    }

    public Vacancy(
            Long id,
            String title,
            String description,
            String company,
            String city,
            String employmentType,
            String experience,
            String salary,
            String workFormat,
            String url,
            LocalDateTime publicationDate,
            LocalDateTime creationDate
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.city = city;
        this.employmentType = employmentType;
        this.experience = experience;
        this.salary = salary;
        this.workFormat = workFormat;
        this.url = url;
        this.publicationDate = publicationDate;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salaryFrom) {
        this.salary = salaryFrom;
    }

    public String getWorkFormat() {
        return workFormat;
    }

    public void setWorkFormat(String workFormat) {
        this.workFormat = workFormat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<UserVacancy> getUserVacancyList() {
        return userVacancyList;
    }

    public void setUserVacancyList(List<UserVacancy> userVacancyList) {
        this.userVacancyList = userVacancyList;
    }

    public void addUserVacancy(UserVacancy userVacancy) {
        userVacancyList.add(userVacancy);
        userVacancy.setVacancy(this);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n", "", ".");

        if (title != null) sj.add("<b>" + title + "</b>");
        if (company != null) sj.add("<i>" + company + "</i>");
//        if (description != null) sj.add(description);
        if (city != null) sj.add("<i>Город:</i> " + city);
        if (experience != null) sj.add("<i>Опыт работы:</i> " + experience);
        if (workFormat != null) sj.add("<i>Формат работы:</i> " + workFormat);
        if (employmentType != null) sj.add("<i>Тип занятости:</i> " + employmentType);
        if (salary != null) sj.add("<i>Зарплата от:</i> " + salary);

        return sj.toString();
    }
}

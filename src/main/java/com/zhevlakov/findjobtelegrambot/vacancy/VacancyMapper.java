package com.zhevlakov.findjobtelegrambot.vacancy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VacancyMapper {
    private final VacancyFormatter vacancyFormatter;

    public VacancyMapper(
            VacancyFormatter vacancyFormatter
    ) {
        this.vacancyFormatter = vacancyFormatter;
    }

    public List<Vacancy> toDomain(List<VacancyDto> vacancyDtoList) {
        return vacancyDtoList.stream()
                .map(dto -> {
                    String fixedDescription = vacancyFormatter.parseHtml(dto.description());

                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(dto.title());
                    vacancy.setDescription(fixedDescription);
                    vacancy.setCompany(dto.company());
                    vacancy.setSalary(dto.salary());
                    vacancy.setUrl(dto.url());
                    vacancy.setPublicationDate(dto.publicationDate());

                    return vacancy;
                }).toList();
    }
}

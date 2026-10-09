package com.zhevlakov.findjobtelegrambot.vacancy.provider.remotive;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemotiveJob(
    String title,
    String url,
    String salary,
    String description,
    @JsonProperty("company_name") String companyName,
    @JsonProperty("publication_date") String publicationDate
) {
}

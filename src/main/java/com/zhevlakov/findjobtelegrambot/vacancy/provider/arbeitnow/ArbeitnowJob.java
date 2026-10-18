package com.zhevlakov.findjobtelegrambot.vacancy.provider.arbeitnow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ArbeitnowJob(
    String title,
    String description,
    String url,
    @JsonProperty("company_name") String companyName,
    Boolean remote,
    String location,
    @JsonProperty("job_types") List<String> jobTypes,
    List<String> tags
) {
}

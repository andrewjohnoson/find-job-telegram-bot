package com.zhevlakov.findjobtelegrambot.vacancy;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class VacancyFormatter {
    public String parseHtml(String input) {
        if (input == null) {
            return "";
        }

        String cleanDescription = Jsoup.parse(input).text();
        if (cleanDescription.length() >= 400) {
            cleanDescription = cleanDescription.substring(0, 400) + "...";
        }

        return fixHtml(cleanDescription);
    }

    private String fixHtml(String input) {
        if (input == null) {
            return "";
        }

        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

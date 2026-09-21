package com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper;

import com.microsoft.playwright.*;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HhPlaywrightScrapper extends AbstractPlaywrightScrapper {
    @Override
    protected List<VacancyDto> parseHtml(Elements vacancies) {
        List<VacancyDto> parsedVacancies = new ArrayList<>();

        for (Element vacancy : vacancies) {
            Element titleElement = vacancy.selectFirst("[data-qa=\"serp-item__title\"]");
            if (titleElement != null) {
                String title = titleElement.text();
                String url = titleElement.attr("href");
                String link = url.startsWith("http") ? url : "https://hh.ru" + url;

                parsedVacancies.add(new VacancyDto(title, link));
            }
        }

        return parsedVacancies;
    }
}

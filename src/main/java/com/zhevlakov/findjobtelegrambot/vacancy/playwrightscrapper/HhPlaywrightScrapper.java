package com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper;

import com.microsoft.playwright.*;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HhPlaywrightScrapper {
    private final Logger log = LoggerFactory.getLogger(HhPlaywrightScrapper.class);

    public List<VacancyDto> fetchVacancies() {
        List<VacancyDto> parsedVacancies = new ArrayList<>();
        try (Playwright playwright = Playwright.create()) {
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setChannel("chrome");

            try (Browser browser = playwright.chromium().launch(launchOptions)) {
                Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                        .setViewportSize(1920, 1080)
                        .setJavaScriptEnabled(true);

                BrowserContext context = browser.newContext(contextOptions);
                Page page = context.newPage();
                page.addInitScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
                String searchQuery = "https://rabota.by/search/vacancy?customDomain=1&overRideDomainAreaId=1002&area=1002&employment_form=FULL&search_field=name&search_field=company_name&search_field=description&work_format=ON_SITE&enable_snippets=true&hhtmFrom=vacancy_search_list&text=Java+Backend+Developer&salary=1000&experience=between1And3&hhtmSource=vacancy_search_list&hhtmSourceLabel=vacancy_search_list&hhtmFromLabel=drawer_filter";
                page.navigate(searchQuery);

                String vacancySelector = "[data-qa=\"vacancy-serp__vacancy\"]";

                try {
                    page.waitForSelector(vacancySelector,
                            new Page.WaitForSelectorOptions().setTimeout(10000));
                } catch (Exception e) {
                    log.error("Произошла ошибка. Возможно, сработала капча {}", e.getStackTrace());
                    return parsedVacancies;
                }

                Locator vacancies = page.locator(vacancySelector);
                for (int i = 0; i < vacancies.count(); i++) {
                    Locator vacancy = vacancies.nth(i);
                    String titleSelector = "[data-qa=\"serp-item__title\"]";
                    Locator titleElement = vacancy.locator(titleSelector);

                    String title = titleElement.innerText();
                    String url = titleElement.getAttribute("href");

                    String link = url.startsWith("http") ? url : "https://hh.ru" + url;
                    parsedVacancies.add(new VacancyDto(title, link));
                }
            }
        } catch (Exception e) {
            log.error("Произошла ошибка. {}", e.getStackTrace());
        }

        return parsedVacancies;
    }
}

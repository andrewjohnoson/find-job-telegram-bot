package com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper;

import com.microsoft.playwright.*;
import com.zhevlakov.findjobtelegrambot.vacancy.VacancyDto;
import com.zhevlakov.findjobtelegrambot.vacancy.playwrightscrapper.exception.ContentNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractPlaywrightScrapper {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public List<VacancyDto> fetchVacancies(String searchQuery, String vacancySelector) {
        log.info("Начата загрузка страницы={}", searchQuery);

        String pageHtml = getPageHtml(searchQuery, vacancySelector);
        if (pageHtml == null || pageHtml.isBlank()) {
            log.error("Не удалось получить HTML сайта. Может быть сработала Captcha/Cloudflare.");
            throw new ContentNotFoundException("Не удалось получить HTML сайта.");
        }

        Document document = Jsoup.parse(pageHtml);
        Elements vacancies = document.select(vacancySelector);

        return parseHtml(vacancies);
    }

    private String getPageHtml(String searchQuery, String vacancySelector) {
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
                page.navigate(searchQuery);

                try {
                    page.waitForSelector(vacancySelector,
                            new Page.WaitForSelectorOptions().setTimeout(10000));
                } catch (Exception e) {
                    log.error("Произошла ошибка. Возможно, сработала капча {}", e.toString());
                    return null;
                }

                return page.content();
            }
        } catch (Exception e) {
            log.error("Произошла ошибка. {}", e.getStackTrace());
            return null;
        }
    }

    protected abstract List<VacancyDto> parseHtml(Elements vacancies);
}

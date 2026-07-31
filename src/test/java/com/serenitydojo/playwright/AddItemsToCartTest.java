package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AddItemsToCartTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--disable-extensions", "--no-sandbox", "--disable-gpu"))
        );
        playwright.selectors().setTestIdAttribute("data-test");
    }

    @BeforeEach
    void setUp(){
        browserContext =  browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext(){
        browserContext.close();
    }

    @AfterAll
    static void tearDownBrowser(){
        browserContext.close();
        playwright.close();
    }

    @DisplayName("Search for pliers")
    @Test
    void searchForPliers() {
        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames =  page.getByTestId("product-name").allTextContents();
        org.assertj.core.api.Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

        Locator outOfStockItems = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");

        assertThat(outOfStockItems).hasCount(1);
        assertThat(outOfStockItems).hasText("Long Nose Pliers");
    }

    @DisplayName("Search for hammer")
    @Test
    void searchForHammer() {
        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").fill("Hammer");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        List<String> productNames =  page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Hammer"));

        Locator priceTag = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("$11.14"))
                .getByTestId("product-name");

        assertThat(priceTag).hasCount(1);
        assertThat(priceTag).hasText("Thor Hammer");
    }

}

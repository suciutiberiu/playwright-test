package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightLocatorsTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll

    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions", "--disble-gpu"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    public void setUp(){
        page = browserContext.newPage();
    }

    @AfterAll
    public static void teardown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Locating elements by text")
    @Nested
    class LocatingElementByText {

        @BeforeEach
        void openTheCatalogPage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @DisplayName("Locationg an element by text contents")
        @Test
        void byText() {
            page.getByText("Bolt Cutters").click();

            assertThat(page.getByText("MightyCraft Hardware")).isVisible();
        }

        @DisplayName("Using alt text")
        @Test
        void byAltText() {
            page.getByAltText("Combination Pliers").click();

            assertThat(page.getByText("ForgeFlex Tools")).isVisible();
        }

        @DisplayName("Using Title")
        @Test
        void byTitle() {
            page.getByAltText("Combination Pliers").click();

            page.getByTitle("Practice Software Testing - Toolshop").click();
        }
    }

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocatingElementByCSS {
        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }


        @DisplayName("By id")
        @Test
        void locateTheFirstNameFieldByID() {
            page.locator("#first_name").fill("Sarah-Jane");
            assertThat(page.locator("#first_name")).hasValue("Sarah-Jane");
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCSSClass() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allInnerTexts();
            Assertions.assertFalse(alertMessages.isEmpty());
        }

        @DisplayName("By attribute")
        @Test
        void locateTheSendButtonByAttribute() {
            page.locator("input [placeholder='Your last name *']").fill("Smith");
            assertThat(page.locator("#last_name")).hasValue("Smith");
        }
    }
}


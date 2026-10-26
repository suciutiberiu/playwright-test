package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightAssertionsTest {

    static Playwright playwright;
    static Browser browser;
    static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("making assertions about the content of a field")
    @Nested
    class LocatingELementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Chacking the value of a field")
        @Test
        void fieldValues() {
            var firstNameField = page.getByLabel("First Name");

            firstNameField.fill("Sarah-Jane");

            assertThat(firstNameField).hasValue("Sarah-Jane");
            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();

        }

        @DisplayName("Checking the value of a text field")
        @Test
        void textField() {
            var messageField = page.getByLabel("Message");

            messageField.fill("This is a message");
            assertThat(messageField).hasValue("This is a message");
            assertThat(messageField).isVisible();
        }

        @DisplayName("Checking the value of a dropdown")
        @Test
        void dropdown() {
            var subjectField = page.getByLabel("Subject");
            assertThat(subjectField).containsText("Select a subject");

            subjectField.selectOption("Payments");
            assertThat(subjectField).hasValue("payments");
            assertThat(subjectField).isVisible();
        }
    }

    @DisplayName("Making assertions aboutr data values")
    @Nested
    class MakingAssertionsAboutDataValues {
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com/");
            page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
        }

        @Test
        void allProductPricesShouldBeCorrectValues() {
            List<Double> prices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$", "")))
                    .toList();
            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(price -> price > 0)
                    .doesNotContain(0.0)
                    .allMatch(price -> price < 1000)
                    .allSatisfy(price -> Assertions.assertThat(price)
                            .isGreaterThan(0.0)
                            .isLessThan(1000.0));
        }

        @Test
        void shouldSortInAlphabeticallyOrder() {
            page.getByLabel("sort").selectOption("Name (A - Z)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productName = page.getByTestId("product-name").allTextContents();

            Assertions.assertThat(productName).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
        }
        @Test
        void shouldSortInReverseAlphabeticallyOrder() {
            page.getByLabel("sort").selectOption("Name (Z - A)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productName = page.getByTestId("product-name").allTextContents();

            Assertions.assertThat(productName).isSortedAccordingTo(Comparator.reverseOrder());
        }

    }
}



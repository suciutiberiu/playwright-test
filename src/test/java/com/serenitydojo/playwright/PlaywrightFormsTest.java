package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightFormsTest {

    @DisplayName("Interact with text fields")
    @Nested
    public class WhenInteractWithTextFields {
        @BeforeEach
        void openContactPage(Page page){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Complete the form")
        @Test
        public void completeForm(Page page) throws URISyntaxException {
            var firstNameField = page.getByLabel("First Name");
            var lastNameField = page.getByLabel("Last Name");
            var emailField = page.getByLabel("Email address");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("John-Arnold");
            lastNameField.fill("Doe");
            emailField.fill("John-Arnold.Doe@cesafac.com");
            messageField.fill("Hello World");
            subjectField.selectOption("Payments");

            Path fileToUpload = Paths.get(ClassLoader.getSystemClassLoader().getResource("data/retete.txt").toURI());

            page.setInputFiles("#attachment", fileToUpload);

            assertThat(firstNameField).hasValue("John-Arnold");
            assertThat(lastNameField).hasValue("Doe");
            assertThat(emailField).hasValue("John-Arnold.Doe@cesafac.com");
            assertThat(messageField).hasValue("Hello World");
            assertThat(subjectField).hasValue("payments");

            String uploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("retete.txt");
        }

    }
}


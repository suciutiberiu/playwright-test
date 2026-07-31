package com.serenitydojo.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.Arrays;

public class HeadlessChromeOptions implements OptionsFactory {
    @Override
    public Options getOptions(){
        return new Options().setLaunchOptions(
                new BrowserType.LaunchOptions()
                    .setArgs(Arrays.asList("--disable-extensions", "--no-sandbox", "--disable-gpu"))
                    ).setHeadless(false)
                        .setTestIdAttribute("data-test");

    }
}

package org.unsplash.exercise.enums;

import java.util.Arrays;

public enum Browser {

    CHROME("Chrome", "Chrome"),
    EDGE("Edge", "Edge"),
    FIREFOX("Firefox", "FF");

    public final String fullName;
    public final String shortName;

    public final static String PROPERTY = "browser";

    Browser(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public static Browser getBrowser(String value) {
        return Arrays.stream(Browser.values())
                .filter(browser -> browser.fullName.trim().equalsIgnoreCase(value) || browser.shortName.equalsIgnoreCase(value))
                .findFirst()
                .orElse(Browser.CHROME);
    }
}

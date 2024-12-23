package pages.commen;

import org.openqa.selenium.By;

public class BaseElement {
    private final By locator;

    public BaseElement(By locator) {
        this.locator = locator;
    }

    public By getLocator() {
        return locator;
    }
}

package steps;

import base.BaseTest;
import constants.Constants;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ClassList;

import java.time.Duration;
import java.util.List;

import static constants.Constants.DEFAULT_MAX_ITERATION_COUNT;
import static constants.Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT;

public class BaseSteps {
    public WebDriver driver;
    public Actions actions;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public BaseSteps() {
        this.driver = BaseTest.getDriver();
        this.actions = new Actions(this.driver);
        ClassList.getInstance().put(this);
    }

    public WebElement findElement(By infoParam) {
        logger.info("Entered. Parameters; infoParam: {}", infoParam);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    public List<WebElement> findElements(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        return driver.findElements(by);
    }

    public void clickElement(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        try {
            clickElement(findElement(by));
        } catch (ElementClickInterceptedException exception) {
            scrollByJs(by);
            clickElement(findElement(by));
        }

    }

    public void clickElement(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        try {
            element.click();
            logger.info("Clicks on the element. Parameters; element: {}", element);
        } catch (ElementClickInterceptedException exception) {
            javaScriptClicker(element);
            logger.info("Caught. Clicks on the element with JS ");
        }
    }

    public void hoverOnTheElementAndClick(By by) {
        logger.info("Entered. Parameters; by: {}", by);
        try {
            hoverElement(findElement(by));
            javaScriptClicker(findElement(by));
            waitForPageToCompleteState();
        } catch (ElementClickInterceptedException exception) {
            logger.info(exception + " has caught");
            scrollByJs(by);
            hoverElement(findElement(by));
            javaScriptClicker(findElement(by));
            waitForPageToCompleteState();
        }
    }

    public void refreshPage() {
        logger.info("Entered.");
        driver.navigate().refresh();
    }


    public void hoverElement(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        actions.moveToElement(element).build().perform();
    }


    public void checkIfElementExistLogCurrentText(By locator) {
        logger.info("Entered. Locator: {}", locator);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (findElements(locator).size() > 0) {
                String elementText = findElement(locator).getText();
                logger.info("Element found. Text: {}", elementText);
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element was not visible with the given locator: " + locator);
    }

    public void javaScriptClicker(WebElement element) {
        logger.info("Entered. Parameters; element: {}", element);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }


    public void waitByMilliSeconds(long milliseconds) {
        logger.info("Entered. Parameters; milliseconds: {}", milliseconds);
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void switchNewWindow() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void goToUrl(String url) {
        logger.info("Entered. Parameters; url: {}", url);
        driver.get(url);
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException ignore) {
            // This is the way to know it wasn't there
        }
        logger.info(url + " adresine gidiliyor.");
    }

    public String getCurrentUrl() {
        logger.info("Entered.");
        return driver.getCurrentUrl();
    }

    public void scrollByJs(By by) {
        logger.info("Entered. Parameters; element: {}", by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", findElement(by));
        waitForPageToCompleteState();
    }


    public void waitForPageToCompleteState() {
        logger.info("Entered.");
        int counter = 0;
        int maxNoOfRetries = Constants.DEFAULT_MAX_ITERATION_COUNT;
        while (counter < maxNoOfRetries) {
            waitByMilliSeconds(Constants.DEFAULT_MILLISECOND_WAIT_AMOUNT);
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
            } catch (Exception ignored) {
            }
            counter++;
        }
    }

    public WebElement findMostExpensiveTablet(List<WebElement> priceElements) {
        double maxPrice = 0.0;
        WebElement mostExpensiveTablet = null;

        for (WebElement priceElement : priceElements) {
            try {
                String priceText = priceElement.getText()
                        .replaceAll("[^0-9,\\.]", "")
                        .replaceAll("\\.", "")
                        .replace(",", ".");

                double price = Double.parseDouble(priceText);

                if (price > maxPrice) {
                    maxPrice = price;
                    mostExpensiveTablet = priceElement;
                }
            } catch (NumberFormatException e) {
                logger.warn("Geçersiz fiyat formatı: " + priceElement.getText());
            }
        }

        if (mostExpensiveTablet != null) {
            logger.info("En yüksek fiyatlı tablet bulundu: " + mostExpensiveTablet.getText());
        } else {
            logger.warn("Hiçbir uygun fiyatlı tablet bulunamadı.");
        }
        return mostExpensiveTablet;
    }

}



package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.web.WebHepsiburadaPage;
import steps.BaseSteps;
import utils.ClassList;
import utils.SharedData;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class CommonStepDefinitions {
    private final BaseSteps baseSteps = ClassList.getInstance().get(BaseSteps.class);
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Given("Go to {string} address")
    public void goToUrl(String url) {
        logger.info("Entered. Parameters; key: {}", url);
        baseSteps.switchNewWindow();
        baseSteps.goToUrl(url);
        logger.info("Current Url: {}", baseSteps.getCurrentUrl());
        baseSteps.waitForPageToCompleteState();
    }

    @And("Accept cookies on the page")
    public void acceptCookies() {
        baseSteps.clickElement(WebHepsiburadaPage.COOKIES_ACCEPT_BUTTON.getLocator());
        waitByMilliSeconds(2000);
    }

    @And("Add products to cart and go to cart")
    public void goToCartAndCheck() {
        baseSteps.clickElement(WebHepsiburadaPage.ADD_TO_CART_BUTTON.getLocator());
        baseSteps.checkIfElementExistLogCurrentText(WebHepsiburadaPage.PRODUCT_IN_CART_TEXT.getLocator());
        waitByMilliSeconds(3000);
        baseSteps.clickElement(WebHepsiburadaPage.GO_TO_CART_BUTTON.getLocator());
    }

    @Given("User wants to hover and click on the {string} category")
    public void test(String categoryName) {
        baseSteps.hoverOnTheElementAndClick(By.xpath(
                String.format("//*[text()='%s']", categoryName)
        ));
    }

    @And("Switch to new window")
    public void switchNewWindow() {
        logger.info("Entered.");
        baseSteps.switchNewWindow();
    }


    @And("User waits {long} milliseconds")
    public void waitByMilliSeconds(long milliseconds) {
        logger.info("Entered. Parameters; milliseconds: {}", milliseconds);
        baseSteps.waitByMilliSeconds(milliseconds);
    }

    @And("Wait {int} seconds")
    public void waitBySeconds(int seconds) {
        logger.info("Entered. Parameters; seconds: {}", seconds);
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("I find the most expensive tablet within the provided elements, validate its price, and click on it")
    public void iFindAndClickMostExpensiveTablet() {
        waitByMilliSeconds(2000);
        List<WebElement> priceElements = baseSteps.findElements(WebHepsiburadaPage.TABLET_PRICE.getLocator());
        if (priceElements.isEmpty()) {
            throw new NoSuchElementException("No price elements found.");
        }

        WebElement mostExpensiveTablet = baseSteps.findMostExpensiveTablet(priceElements);
        Assert.assertNotNull("No tablet found within the provided elements.", mostExpensiveTablet);

        String priceText = mostExpensiveTablet.getText();
        Assert.assertFalse("The price text should not be empty.", priceText.isEmpty());
        logger.info("Most expensive tablet's price is: {}", priceText);

        baseSteps.clickElement(mostExpensiveTablet);
        logger.info("Clicked on the most expensive tablet.");
    }


    @When("Save Information on Product Detail Page for Most Expensive Tablet")
    public void saveMostExpensiveTabletDetails() {
        String priceText = baseSteps.findElement(WebHepsiburadaPage.TABLET_PRICE.getLocator()).getText();
        String titleText = baseSteps.findElement(WebHepsiburadaPage.PRODUCT_TITLE.getLocator()).getText();
        SharedData.saveData("productDetailPrice", priceText);
        SharedData.saveData("productDetailTitle", titleText);
    }

    @When("Compare Product Details from Product Page with Cart")
    public void compareProductDetailsWithCart() {
        String productPriceInCardText = baseSteps.findElement(WebHepsiburadaPage.CART_PRODUCT_PRICE.getLocator()).getText();
        String productTitleInCardText = baseSteps.findElement(WebHepsiburadaPage.CART_PRODUCT_TITLE.getLocator()).getText();
        String productDetailPrice = SharedData.getData(String.class, "productDetailPrice");
        String productDetailTitle = SharedData.getData(String.class, "productDetailTitle");

        try {
            assert productDetailPrice.equalsIgnoreCase(productPriceInCardText) :
                    "Product price mismatch! Detail Page: " + productDetailPrice + ", Cart: " + productPriceInCardText;
            assert productDetailTitle.equalsIgnoreCase(productTitleInCardText) :
                    "Product title mismatch! Detail Page: " + productDetailTitle + ", Cart: " + productTitleInCardText;
            logger.info("Assertions passed: Product details are consistent between detail page and cart.");
        } catch (AssertionError e) {
            logger.error("Assertion failed: Product details mismatch. Expected price: {}, Cart price: {}. Expected title: {}, Cart title: {}",
                    productDetailPrice, productPriceInCardText, productDetailTitle, productTitleInCardText, e);
            throw e;
        }

    }

    @Given("User wants to hover and click on the categories")
    public void clickElement(DataTable table) {
        List<Map<String, String>> actions = table.asMaps(String.class, String.class);

        for (Map<String, String> action : actions) {
            String hoverCategory = action.get("Hover");
            if (hoverCategory != null && !hoverCategory.isEmpty()) {
                logger.info("Hovering on the category: {}", hoverCategory);
                baseSteps.hoverElement(baseSteps.findElement(By.xpath(String.format("//*[normalize-space(text())='%s']", hoverCategory))));
            }
        }

        for (Map<String, String> action : actions) {
            String clickCategory = action.get("Click");
            if (clickCategory != null && !clickCategory.isEmpty()) {
                logger.info("Clicking on the category: {}", clickCategory);
                baseSteps.hoverOnTheElementAndClick(By.xpath(String.format("//*[normalize-space(text())='%s']", clickCategory)));
            }
        }
        baseSteps.refreshPage();
    }
}

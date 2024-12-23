package pages.web;

import org.openqa.selenium.By;
import pages.commen.BaseElement;

public class WebHepsiburadaPage extends WebMainPage {

    public static final String PAGE_NAME = "Hepsiburada Page";

    public static final BaseElement COOKIES_ACCEPT_BUTTON = new BaseElement(By.xpath("//button[@id='onetrust-accept-btn-handler']"));
    public static final BaseElement TABLET_PRICE = new BaseElement(By.xpath("//div[@data-test-id='price-current-price']"));
    public static final BaseElement ADD_TO_CART_BUTTON = new BaseElement(By.xpath("//button[@data-test-id='addToCart']"));
    public static final BaseElement PRODUCT_TITLE = new BaseElement(By.xpath("//h1[@data-test-id='title']"));
    public static final BaseElement PRODUCT_IN_CART_TEXT = new BaseElement(By.xpath("//span[contains(@class,'checkoutui')]"));
    public static final BaseElement GO_TO_CART_BUTTON = new BaseElement(By.xpath("//button[text()='Sepete git']"));
    public static final BaseElement CART_PRODUCT_TITLE = new BaseElement(By.xpath("//div[contains(@class,'product_name')]//a"));
    public static final BaseElement CART_PRODUCT_PRICE = new BaseElement(By.xpath("(//ul[contains(@class,'item_list')]//div[contains(@class,'product_price')])[last()]"));

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }
}

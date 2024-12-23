package pages.web;

import pages.commen.BaseElement;
import pages.commen.BaseMainPage;

import java.util.HashMap;
import java.util.Map;

public abstract class WebMainPage extends BaseMainPage {
    private static final Map<String, WebMainPage> WEB_PAGES = new HashMap<>();

    static {
        WEB_PAGES.put("Hepsiburada Page", new WebHepsiburadaPage());
    }

    public static WebMainPage getPage(String pageName) {
        if (WEB_PAGES.containsKey(pageName)) {
            return WEB_PAGES.get(pageName);
        }
        throw new IllegalArgumentException("No web page found for name: " + pageName);
    }

    public BaseElement getBy(String elementName) {
        try {
            return (BaseElement) this.getClass().getField(elementName).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Element '" + elementName + "' not found in page '" + this.getPageName() + "'.", e);
        }
    }
}

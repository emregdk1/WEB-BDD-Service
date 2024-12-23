package utils;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@test1",
        plugin = {"pretty"},
        features = "classpath:features",
        glue = {
                "base",
                "stepDefinitions",

        },
        publish = true
)
public class CucumberRunner {
}

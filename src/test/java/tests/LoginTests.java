package tests;

import config.ConfigHelper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;

public class LoginTests extends TestBase {

    @Test
    void loginWithCookieTest() {
        // authorize
        Map<String, String> cookiesMap =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .formParam("Email", ConfigHelper.getUsername())
                        .formParam("Password", ConfigHelper.getPassword())
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(302)
                        .log().body()
                        .extract().cookies();

        // verify successful authorization
        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(new Cookie("Nop.customer", cookiesMap.get("Nop.customer")));
        getWebDriver().manage().addCookie(new Cookie("NOPCOMMERCE.AUTH", cookiesMap.get("NOPCOMMERCE.AUTH")));
        getWebDriver().manage().addCookie(new Cookie("ARRAffinity", cookiesMap.get("ARRAffinity")));

        open("");
        $(".account").shouldHave(text(ConfigHelper.getUsername()));
    }
}
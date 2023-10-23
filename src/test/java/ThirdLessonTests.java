import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class ThirdLessonTests {
    @ParameterizedTest
    @ValueSource(strings = {"", "qwertyqwerty123", "qwerty1231231231", "qwerty12312312312", "qwertyqwerty12", "qwerty1", "gregergerggrgegergergergergergergergergergegrgergerger"})
    public void FirstHomeWork(String name) {

        assertTrue(name.length() > 15, "Маленькое сообщение, не подходит");


    }

    @ParameterizedTest
    @ValueSource(strings = {"cookies", "headers"})
    public void SecondAndThirdHomeWork(String condition) {
        Map<String, String> cookies;

        if (condition.equals("cookies")) {
            Response response = RestAssured
                    .get("https://playground.learnqa.ru/api/homework_cookie")
                    .andReturn();
            cookies = response.getCookies();
            String homeWorkCookie = (response.getCookie("HomeWork"));
            assertTrue(cookies.containsKey("HomeWork"), "We can't find a cookie for HW");
            assertTrue(homeWorkCookie.equals("hw_value"), "We have smth in a cookie");
            System.out.println(homeWorkCookie);
        } else if (condition.equals("headers")) {
            Response response = RestAssured
                    .get("https://playground.learnqa.ru/api/homework_header")
                    .andReturn();
            System.out.println(response.getHeader("X-Secret-Homework-Header"));
            assertTrue(response.getHeaders().hasHeaderWithName("X-Secret-Homework-Header"));
            assertTrue(response.getHeader("X-Secret-Homework-Header").equals("Some secret value"), "We don't have a value");

        } else {
            throw new IllegalArgumentException("We don't have this condition in HW");
        }
    }
    @ParameterizedTest
    @CsvSource(
            {
                    "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', Mobile, Chrome, iOS",
                    "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', Googlebot, Unknown, Unknown",
                    "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', Web, Chrome, No",
                    "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', Mobile, No, iPhone",
                    "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', Mobile, No, Android"
            }


    )
    public void fourthTest(String userAgent, String expectedPlatform, String  expectedBrowser, String expectedDevice){
        Map<String, String> cookies = new HashMap<>();
        cookies.put("User-Agent", userAgent);
        JsonPath response = RestAssured
                .given()
                .headers(cookies)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        response.prettyPrint();
        assertTrue(response.getString("platform").equals(expectedPlatform), "Platform is incorrect for " + userAgent);
        assertTrue(response.getString("browser").equals(expectedBrowser), "Browser is incorrect for " + userAgent);
        assertTrue(response.getString("device").equals(expectedDevice), "Device is incorrect for " + userAgent);
    }


}

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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


}

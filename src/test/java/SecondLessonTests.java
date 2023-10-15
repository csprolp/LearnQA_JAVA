import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.lang.Thread.sleep;

public class SecondLessonTests {
    @Test
    public void getJsonHomework() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<LinkedHashMap> listOfMessages = response.getList("messages");
        LinkedHashMap secondMessage = listOfMessages.get(1);
        System.out.println(secondMessage);
    }

    @Test
    public void redirectLocation() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        System.out.println((response.getStatusCode()));
        System.out.println(response.getHeader("location"));
    }

    @Test
    public void longRedirectLocation() {
        int i = 0;
        Response response;
        String link = "https://playground.learnqa.ru/api/long_redirect";
        do {

            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(link)

                    .andReturn();
            i++;
            System.out.println((response.getStatusCode()));
            System.out.println(response.getHeader("location"));
            link = response.getHeader("location");
            System.out.println(i);
        }
        while (response.statusCode() != 200);

    }

    @Test
    public void tokensHomeWork() {
        JsonPath response;
        String link = "https://playground.learnqa.ru/ajax/api/longtime_job";
        Map<String, String> data = new HashMap<>();

        response = RestAssured
                .get(link)
                .jsonPath();
        Integer seconds = (response.getInt("seconds"));
        String token = response.getString("token");
        System.out.println(seconds);
        System.out.println(token);
        data.put("token", token);
        try {
            sleep((seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        response = RestAssured
                .given()
                .queryParams(data)
                .get(link)
                .jsonPath();
        System.out.println(response.getString("status"));
        System.out.println(response.getString("result"));
    }
    @Test
    public void lastHomeWork(){
        Response responseLogin;
        Response responseCheckCookie;
        Map<String, String> data = new HashMap<>();
        Map<String, String> cookies = new HashMap<>();
        String[] array = new String[]{"123456", "password", "12345678", "qwerty", "12345", "123456789", "football", "1234", "1234567", "baseball", "welcome", "1234567890", "abc123", "111111", "1qaz2wsx", "dragon", "master", "monkey", "letmein", "login", "princess", "qwertyuiop", "solo", "passw0rd", "starwars", "123456", "password", "12345", "12345678", "football", "qwerty", "1234567890", "1234567", "princess", "1234", "login", "welcome", "solo", "abc123", "admin", "121212", "flower", "passw0rd", "dragon", "sunshine", "master", "hottie", "loveme", "zaq1zaq1", "password1", "123456", "password", "12345678", "qwerty", "abc123", "123456789", "111111", "1234567", "iloveyou", "adobe123[a]", "123123", "admin", "1234567890", "letmein", "photoshop[a]", "1234", "monkey", "shadow", "sunshine", "12345", "password1", "princess", "azerty", "trustno1", "0", "123456", "password", "12345", "12345678", "qwerty", "123456789", "1234", "baseball", "dragon", "football", "1234567", "monkey", "letmein", "abc123", "111111", "mustang", "access", "shadow", "master", "michael", "superman", "696969", "123123", "batman", "trustno1", "password", "123456", "12345678", "qwerty", "abc123", "monkey", "1234567", "letmein", "trustno1", "dragon", "baseball", "111111", "iloveyou", "master", "sunshine", "ashley", "bailey", "passw0rd", "shadow", "123123", "654321", "superman", "qazwsx", "michael", "Football", "password", "123456", "12345678", "abc123", "qwerty", "monkey", "letmein", "dragon", "111111", "baseball", "iloveyou", "trustno1", "1234567", "sunshine", "master", "123123", "welcome", "shadow", "ashley", "football", "jesus", "michael", "ninja", "mustang", "password1", "123456", "password", "12345678", "qwerty", "12345", "123456789", "letmein", "1234567", "football", "iloveyou", "admin", "welcome", "monkey", "login", "abc123", "starwars", "123123", "dragon", "passw0rd", "master", "hello", "freedom", "whatever", "qazwsx", "trustno1", "123456", "password", "123456789", "12345678", "12345", "111111", "1234567", "sunshine", "qwerty", "iloveyou", "princess", "admin", "welcome", "666666", "abc123", "football", "123123", "monkey", "654321", "!@#$%^&*", "charlie", "aa123456", "donald", "password1", "qwerty123", "123456", "123456789", "qwerty", "password", "1234567", "12345678", "12345", "iloveyou", "111111", "123123", "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321", "555555", "lovely", "7777777", "welcome", "888888", "princess", "dragon", "password1", "123qwe"};
        int i = 0;
        String result = "You are authorized";
        do
        {
            data.put("login", "super_admin");
            data.put("password", array[i]);
            responseLogin = RestAssured
                    .given()
                    .queryParams(data)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            String cookie = responseLogin.cookie("auth_cookie");

            cookies.put("auth_cookie", cookie);
            responseCheckCookie = RestAssured
                    .given()
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            i++;
        }
        while (!responseCheckCookie.print().equals(result));
        System.out.println("Password is: " + array[i-1]);
    }
}

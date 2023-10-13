import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        int i = 0;
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
}

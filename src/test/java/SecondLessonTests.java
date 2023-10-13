import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.LinkedHashMap;
import java.util.List;

public class SecondLessonTests {
    @Test
    public void getJsonHomework(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<LinkedHashMap> listOfMessages = response.getList("messages");
        LinkedHashMap secondMessage = listOfMessages.get(1);
         System.out.println(secondMessage);
    }

    @Test
    public void redirectLocation(){
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
}

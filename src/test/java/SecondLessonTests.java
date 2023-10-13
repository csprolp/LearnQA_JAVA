import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
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


}

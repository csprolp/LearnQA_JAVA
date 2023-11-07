package tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("CRUD")
@Feature("Get info")
public class userGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.AssertJsonHasField(responseUserData, "username");
        Assertions.AssertJsonHasNotField(responseUserData, "firstName");
        Assertions.AssertJsonHasNotField(responseUserData, "lastName");
        Assertions.AssertJsonHasNotField(responseUserData, "email");

    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response response = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String header = this.getHeader(response, "x-csrf-token");
        String cookie = this.getCookie(response, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.AssertJsonHasFields(responseUserData, expectedFields);
    }

    @Description("This test tried to get a info of another user")
    @DisplayName("Test negative to get info by anotherr User")
    @Severity(SeverityLevel.CRITICAL)
    @Tags({@Tag("GET"), @Tag("Regression")})
    @Test
    public void testGetUserInfoByAnotherUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        apiCoreRequests.makePostRequestForAuth("https://playground.learnqa.ru/api/user/login", authData);
        Response responseGet = apiCoreRequests.makeGetRequestForInfoOfUserById("https://playground.learnqa.ru/api/user/", 2);
        System.out.println(responseGet.asString());
        Assertions.AssertJsonHasField(responseGet, "username");
        String[] unexpectedFields = {"firstName", "lastName", "email"};
        Assertions.AssertJsonHasNotFields(responseGet, unexpectedFields);
    }

}

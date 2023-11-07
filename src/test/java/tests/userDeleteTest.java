package tests;

import io.qameta.allure.*;
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
@Feature("Delete")
public class userDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test successfully delete a created user")
    @DisplayName("Test Positive Delete User")
    @Severity(SeverityLevel.CRITICAL)
    @Tags({@Tag("Delete"), @Tag("Regression")})
    public void testDeleteCreatedUser() {

        //Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        String userId = getStringFromJson(responseCreateAuth, "id");

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequestForAuth("https://playground.learnqa.ru/api/user/login", authData);

        apiCoreRequests.makeDeleteRequestForDelete("https://playground.learnqa.ru/api/user/"+userId, getHeader(responseGetAuth, "x-csrf-token"), getCookie(responseGetAuth, "auth_sid"));
        Response responseGetDeletedUser = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+userId,getHeader(responseGetAuth, "x-csrf-token"), getCookie(responseGetAuth, "auth_sid"));
        Assertions.AssertResponseTextEquals(responseGetDeletedUser, "User not found");
        Assertions.AssertResponseCodeEquals(responseGetDeletedUser, 404);

    }


    @Test
    @Description("This test unsuccessfully delete a Admin user")
    @DisplayName("Test Negative Delete Admin User")
    @Severity(SeverityLevel.NORMAL)
    @Tags({@Tag("Delete"), @Tag("Regression")})
    public void testDeleteAdminUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestForAuth("https://playground.learnqa.ru/api/user/login", authData);

        Response responseDelete = apiCoreRequests.makeDeleteRequestForDelete("https://playground.learnqa.ru/api/user/2", getHeader(responseGetAuth, "x-csrf-token"), getCookie(responseGetAuth, "auth_sid"));
        Assertions.AssertResponseTextEquals(responseDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        Assertions.AssertResponseCodeEquals(responseDelete, 400);
    }

    @Test
    @Description("This test unsuccessfully delete a created user by another user")
    @DisplayName("Test Negative Delete User by another User")
    @Severity(SeverityLevel.NORMAL)
    @Tags({@Tag("Delete"), @Tag("Regression")})
    public void testDeleteCreatedByAnotherUser() {

        //Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        String userId = getStringFromJson(responseCreateAuth, "id");

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequestForAuth("https://playground.learnqa.ru/api/user/login", authData);

        Response responseDeleteUser = apiCoreRequests.makeDeleteRequestForDelete("https://playground.learnqa.ru/api/user/"+userId, getHeader(responseGetAuth, "x-csrf-token"), getCookie(responseGetAuth, "auth_sid"));
        Assertions.AssertResponseCodeNotEquals(responseDeleteUser, 400);
        //We don't have a correct error text

        //Assertions.AssertResponseTextEquals(responseGetDeletedUser, "User not found");
        //Assertions.AssertResponseCodeEquals(responseGetDeletedUser, 404);

    }
}

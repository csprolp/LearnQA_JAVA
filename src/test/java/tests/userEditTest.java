package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("CRUD")
@Feature("Edit")
public class userEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditWithoutAuth() {
//Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        String userId = getStringFromJson(responseCreateAuth, "id");

        //Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response editResponse = apiCoreRequests.makePutRequestForEdit("https://playground.learnqa.ru/api/user/" + userId, editData);
        Assertions.AssertResponseTextEquals(editResponse, "Auth token not supplied");
        Assertions.AssertResponseCodeEquals(editResponse, 400);

    }

    @Test
    public void testEditWithAuthByAnotherUser() {
//Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        String userId = getStringFromJson(responseCreateAuth, "id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests.makePostRequestForAuth("https://playground.learnqa.ru/api/user/login", authData);
        String cookie = getCookie(responseGetAuth, "auth_sid");
        String header = getHeader(responseGetAuth, "x-csrf-token");

        //Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response editResponse = apiCoreRequests.makePutRequestForEdit("https://playground.learnqa.ru/api/user/" + userId, editData, header, cookie);
        System.out.println(editResponse.statusCode());
        System.out.println(editResponse.asString());
        //We don't know a correct error
        //Assertions.AssertResponseTextEquals(editResponse, "Auth token not supplied");
        Assertions.AssertResponseCodeEquals(editResponse, 400);

    }


    @Test
    public void testEditWithIncorrectEmailTest() {
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

        //Edit
        String newEmail = "changedemail.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequestForEdit("https://playground.learnqa.ru/api/user/" + userId, editData, getHeader(responseGetAuth, "x-csrf-token"), getCookie(responseGetAuth, "auth_sid"));
        Assertions.AssertResponseTextEquals(responseEditUser, "Invalid email format");
        Assertions.AssertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    public void testEditWithIncorrectFirstNameTest() {
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

        //Edit
        String firstName = "1";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", firstName);

        Response responseEditUser = apiCoreRequests.makePutRequestForEdit("https://playground.learnqa.ru/api/user/" + userId, editData, getHeader(responseGetAuth, "x-csrf-token"), getCookie(responseGetAuth, "auth_sid"));

        Assertions.AssertJsonByName(responseEditUser, "error", "Too short value for field firstName");
        Assertions.AssertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    public void testEditJustCreatedTest() {
//Generate User
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        //Login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //Edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        Assertions.AssertJsonByName(responseUserData, "firstName", newName);
    }
}

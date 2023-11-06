package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Register classes")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.AssertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.AssertResponseCodeEquals(responseCreateAuth, 400);
    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        //Assertions.AssertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
        Assertions.AssertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.AssertJsonHasField(responseCreateAuth, "id");
    }

    @Description("This test checks registration  w/o correct email")
    @DisplayName("Test negative reg user")
    @Test
    public void testCreateUserWithIncorrectEmail() {
        String email = DataGenerator.getIncorrectRandomEmail();
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.put("email", email);
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        Assertions.AssertResponseTextEquals(responseCreateAuth, "Invalid email format");
        Assertions.AssertResponseCodeEquals(responseCreateAuth, 400);

    }

    @Description("This test checks registration  w/o necessary field")
    @DisplayName("Test negative reg user")
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
    public void testCreateUserWithOutNecessaryFields(String condition) {

        Map<String, String> userData = DataGenerator.getRegistrationData();


        userData.remove(condition);
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        Assertions.AssertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + condition);
        Assertions.AssertResponseCodeEquals(responseCreateAuth, 400);

    }
    @Description("This test checks registration  with incorrect username, long or short")
    @DisplayName("Test negative reg user")
    @ParameterizedTest
    @ValueSource(strings = {"long", "short"})
    public void testCreateUserWithIncorrectUsername(String condition) {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        if (condition.equals("long")) {
            String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            userData.put("username",symbols+symbols+symbols+symbols+symbols);
        } else if (condition.equals("short")) {
            userData.put("username", "A");
        }else {throw new IllegalArgumentException("Incorrect"+ condition);}
        Response responseCreateAuth = apiCoreRequests.makePostRequestForRegister("https://playground.learnqa.ru/api/user/", userData);
        Assertions.AssertResponseTextEquals(responseCreateAuth,"The value of 'username' field is too "+ condition);
        Assertions.AssertResponseCodeEquals(responseCreateAuth, 400);
    }
}

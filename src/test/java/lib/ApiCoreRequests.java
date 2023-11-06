package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a get request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie(("auth_sid"), cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a get request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie(("auth_sid"), cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a get request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a post request for Auth")
    public Response makePostRequestForAuth(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a post request for Registration")
    public Response makePostRequestForRegister(String url, Map<String, String> registerData) {
        return given()
                .filter(new AllureRestAssured())
                .body(registerData)
                .post(url)
                .andReturn();
    }

    @Step("Make a Get request for Info by Id")
    public Response makeGetRequestForInfoOfUserById(String url, int id) {
        return given()
                .filter(new AllureRestAssured())
                .get(url + id)
                .andReturn();
    }
}

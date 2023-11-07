package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void AssertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(value, expectedValue, "Not equal values");
    }

    public static void AssertJsonByName(Response response, String name, String expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        String value = response.jsonPath().getString(name);
        assertEquals(value, expectedValue, "Not equal values");
    }

    public static void AssertResponseTextEquals(Response response, String expectedAnwser) {
        assertEquals(
                expectedAnwser,
                response.asString(),
                "Response text is not as expected"
        );

    }

    public static void AssertResponseTextWithPrettyPrintEquals(Response response, String expectedAnwser) {
        assertEquals(
                expectedAnwser,
                response.prettyPrint(),
                "Response text is not as expected"
        );

    }

    public static void AssertResponseCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Response status code is not as expected"
        );
    }

    public static void AssertJsonHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void AssertJsonHasFields(Response response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.AssertJsonHasField(response, expectedFieldName);
        }
    }

    public static void AssertJsonHasNotField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void AssertJsonHasNotFields(Response response, String[] unexpectedFieldNames) {
        for (String unexpectedFieldName : unexpectedFieldNames) {
            Assertions.AssertJsonHasNotField(response, unexpectedFieldName);
        }
    }


}

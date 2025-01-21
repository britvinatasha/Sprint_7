package base;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;


public class BaseHttpClient {
        protected final RequestSpecification requestSpecification = given()
                .baseUri(Constants.HOST_URL)
                .header("Content-type", "application/json");
    }


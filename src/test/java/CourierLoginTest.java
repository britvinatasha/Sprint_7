import com.github.javafaker.Faker;
import courier.CourierSteps;
import courier.CreateCourier;
import courier.LoginCourier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class CourierLoginTest extends CourierSteps {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();

    @Test
    @DisplayName("Авторизация курьера с валидными данными")
    @Description("Проверка, что можно авторизовать курьера с валидными данными")
    public void testLoginCourierWithValidData() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login, password);
        getRequestForCreatingCourier(courierCreate);
        Response response = getRequestToAuthorizeCourier(courierLogin);
        checkResponseAfterCourierAuthorized(response);
    }

    @Test
    @DisplayName("Авторизация курьера с без логина")
    @Description("Проверка, что невозможно авторизоваться курьера с пустым полем 'login'")
    public void testLoginCourierWithoutLogin() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier("", password);
        getRequestForCreatingCourier(courierCreate);
        Response response = getRequestToAuthorizeCourier(courierLogin);
        checkResponseAfterAuthorizationCourierWithoutLoginOrPassword(response);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Проверка, что невозможно авторизовать курьера с пустым полем 'password'")
    public void testLoginCourierWithoutPassword() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login,"");
        getRequestForCreatingCourier(courierCreate);
        Response response = getRequestToAuthorizeCourier(courierLogin);
        checkResponseAfterAuthorizationCourierWithoutLoginOrPassword(response);
    }

    @Test
    @DisplayName("Авторизация курьера c невалидным логином")
    @Description("Проверка, что невозможно авторизовать курьера с несуществующим логином")
    public void testLoginCourierWithWrongLogin() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login + "qwerty" ,password);
        getRequestForCreatingCourier(courierCreate);
        Response response = getRequestToAuthorizeCourier(courierLogin);
        checkResponseAfterAuthorizationCourierWithWrongLoginOrPassword(response);
    }

    @Test
    @DisplayName("Авторизация курьера c невалидным паролем")
    @Description("Проверка, что невозможно авторизовать курьера с несуществующим паролем")
    public void testLoginCourierWithWrongPassword() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login,password +"qwerty");
        getRequestForCreatingCourier(courierCreate);
        Response response = getRequestToAuthorizeCourier(courierLogin);
        checkResponseAfterAuthorizationCourierWithWrongLoginOrPassword(response);
    }

    @After
    public void cleanData() {
        LoginCourier courierLogin = new LoginCourier(login, password);
        Response response = getRequestToAuthorizeCourier(courierLogin);
        int status = response.then().extract().statusCode();
        if (status == 200) {
            String courierId = response.then().extract().body().path("id").toString();
            deleteCourier(courierId);
        }
    }
}

import com.github.javafaker.Faker;
import courier.CourierSteps;
import courier.CreateCourier;
import courier.LoginCourier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


public class CourierCreateTest extends CourierSteps {
        Faker faker = new Faker();
        String login = faker.name().username();
        String password = faker.internet().password();
        String firstName = faker.name().firstName();


        @Test
        @DisplayName("Создание курьера")
        @Description("Проверка, что можно создать курьера с валидными данными")
        public void testCreateCourierWithValidData() {
                CreateCourier courierCreate = new CreateCourier(login, password, firstName);
                Response response = getRequestForCreatingCourier(courierCreate);
                checkResponseAfterCreatingCourier(response);
        }

        @Test
        @DisplayName("Создание курьера без логина")
        @Description("Проверка, что невозможно создать курьера с пустым полем 'login'")
        public void testCreateCourierWithEmptyLogin() {
                CreateCourier courierCreate = new CreateCourier("", password, firstName);
                Response response = getRequestForCreatingCourier(courierCreate);
                checkResponseAfterCreatingCourierWithoutLoginOrPassword(response);
        }

        @Test
        @DisplayName("Создание курьера без пароля")
        @Description("Проверка, что невозможно создать курьера с пустым полем 'password'")
        public void testCreateCourierWithEmptyPassword() {
                CreateCourier courierCreate = new CreateCourier(login, "", firstName);
                Response response = getRequestForCreatingCourier(courierCreate);
                checkResponseAfterCreatingCourierWithoutLoginOrPassword(response);
        }

        @Test
        @DisplayName("Создание двух одинаковых курьеров")
        @Description("Проверка, что невозможно создать двух курьеров с повторяющимися логинами")
        public void testCreateDuplicateCouriers() {
                CreateCourier courierCreate = new CreateCourier(login, password, firstName);
                Response responseFirst = getRequestForCreatingCourier(courierCreate);
                checkResponseAfterCreatingCourier(responseFirst);
                Response responseSecond = getRequestForCreatingCourier(courierCreate);
                checkResponseAfterCreatingCourierWithDuplicatedLogin(responseSecond);
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

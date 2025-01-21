import base.BaseTest;
import courier.CourierSteps;
import courier.CreateCourier;
import courier.LoginCourier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;


public class CourierCreateTest extends BaseTest {
        CourierSteps courierSteps = new CourierSteps();
        String login = faker.name().username();
        String password = faker.internet().password();
        String firstName = faker.name().firstName();


        @Test
        @DisplayName("Создание курьера")
        @Description("Проверка, что можно создать курьера с валидными данными")
        public void testCreateCourierWithValidData() {
                CreateCourier courierCreate = new CreateCourier(login, password, firstName);
                Response response = courierSteps.getRequestForCreatingCourier(courierCreate);
                courierSteps.checkResponseAfterCreatingCourier(response);
        }

        @Test
        @DisplayName("Создание курьера без логина")
        @Description("Проверка, что невозможно создать курьера с пустым полем 'login'")
        public void testCreateCourierWithEmptyLogin() {
                CreateCourier courierCreate = new CreateCourier("", password, firstName);
                Response response = courierSteps.getRequestForCreatingCourier(courierCreate);
                courierSteps.checkResponseAfterCreatingCourierWithoutLoginOrPassword(response);
        }

        @Test
        @DisplayName("Создание курьера без пароля")
        @Description("Проверка, что невозможно создать курьера с пустым полем 'password'")
        public void testCreateCourierWithEmptyPassword() {
                CreateCourier courierCreate = new CreateCourier(login, "", firstName);
                Response response = courierSteps.getRequestForCreatingCourier(courierCreate);
                courierSteps.checkResponseAfterCreatingCourierWithoutLoginOrPassword(response);
        }

        @Test
        @DisplayName("Создание двух одинаковых курьеров")
        @Description("Проверка, что невозможно создать двух курьеров с повторяющимися логинами")
        public void testCreateDuplicateCouriers() {
                CreateCourier courierCreate = new CreateCourier(login, password, firstName);
                Response responseFirst = courierSteps.getRequestForCreatingCourier(courierCreate);
                courierSteps.checkResponseAfterCreatingCourier(responseFirst);
                Response responseSecond = courierSteps.getRequestForCreatingCourier(courierCreate);
                courierSteps.checkResponseAfterCreatingCourierWithDuplicatedLogin(responseSecond);
        }

        @After
        public void cleanData() {
                LoginCourier courierLogin = new LoginCourier(login, password);
                Response response = courierSteps.getRequestToAuthorizeCourier(courierLogin);
                int status = response.then().extract().statusCode();
                if (status == 200) {
                        String courierId = response.then().extract().body().path("id").toString();
                        courierSteps.deleteCourier(courierId);
                }
        }
}

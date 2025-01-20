package courier;
import base.BaseHttpClient;
import base.Constants;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps extends BaseHttpClient {
    @Step("Создание курьера")
    public Response getRequestForCreatingCourier(CreateCourier courier) {
        return requestSpecification
                .and()
                .body(courier)
                .when()
                .post(Constants.CREATE_COURIER);
    }

    @Step("При успешном создании учетной записи: Статус ответа: 201, поле 'ok': true")
        public void checkResponseAfterCreatingCourier(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .assertThat()
                .body("ok",equalTo(true));
    }

    @Step("При создании учетной записи без логина или пароля: Статус ответа Статус ответа: 400, поле 'message': 'Недостаточно данных для создания учетной записи'")
        public void checkResponseAfterCreatingCourierWithoutLoginOrPassword (Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("При создании учетной записи с повторяющимся логином: Статус ответа: 409, поле 'message': 'Этот логин уже используется. Попробуйте другой.'")
        public void checkResponseAfterCreatingCourierWithDuplicatedLogin(Response response) {
        response.then()
                .statusCode(SC_CONFLICT)
                .assertThat()
                .body("message",equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Авторизация курьера")
        public Response getRequestToAuthorizeCourier(LoginCourier courier) {
        return requestSpecification
                .and()
                .body(courier)
                .when()
                .post(Constants.LOGIN_COURIER);
    }

    @Step("При успешной авторизации: Статус ответа: 200, поле 'id' с данными")
        public void checkResponseAfterCourierAuthorized(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("id",notNullValue());
    }

    @Step("При авторизации без логина или пароля: Статус ответа: 400, поле поле 'message': 'Недостаточно данных для входа'")
        public void checkResponseAfterAuthorizationCourierWithoutLoginOrPassword(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("При авторизации с несуществующей парой логин-пароль: Статус ответа: 404, поле 'message': 'Учетная запись не найдена'")
        public void checkResponseAfterAuthorizationCourierWithWrongLoginOrPassword(Response response) {
        response.then()
                .statusCode(SC_NOT_FOUND)
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Удаление курьера")
    public void deleteCourier(String id) {
        requestSpecification
                .delete(Constants.DELETE_COURIER, id)
                .then()
                .statusCode(SC_OK);
    }
}


package order;
import base.BaseHttpClient;
import base.Constants;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps extends BaseHttpClient {
    @Step("Создание заказа")
    public Response getRequestForCreatingOrder (CreateOrder order) {
        return requestSpecification
                .and()
                .body(order)
                .when()
                .post(Constants.CREATE_ORDER);
    }

    @Step("При успешном создании заказа: Статус ответа: 201, поле 'track' с данными")
    public String checkResponseAfterCreatingOrder (Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .assertThat()
                .body("track",notNullValue());
        return response.then().extract().body().path("track").toString();
    }

    @Step("Заказ отменен")
    public void cancelOrder(String track) {
        requestSpecification
                .queryParam("track", track)
                .put(Constants.CANCEL_ORDER)
                .then().statusCode(SC_OK);
    }


    @Step("Получение списка заказов")
    public Response getRequestToGetOrderList() {
        return requestSpecification
                .when()
                .get(Constants.ORDER_LIST);
    }

    @Step("При получении списка заказов: Статус ответа: 200, ответ содержит заказы")
    public void checkResponseAfterGettingOrderList (Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("orders.id", notNullValue());
    }
}

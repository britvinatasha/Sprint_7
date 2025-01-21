import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderSteps;
import org.junit.Test;

public class OrderListTest extends BaseTest {
    OrderSteps orderSteps = new OrderSteps();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что список заказов не пуст")
    public void checkOrderListIsNotEmpty() {
        Response response = orderSteps.getRequestToGetOrderList();
        orderSteps.checkResponseAfterGettingOrderList(response);
    }
}

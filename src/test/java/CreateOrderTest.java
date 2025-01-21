import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.CreateOrder;
import order.OrderSteps;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {
    OrderSteps orderSteps = new OrderSteps();
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    String address = faker.address().fullAddress();
    Integer metroStation = faker.random().nextInt(4);
    String phone = faker.phoneNumber().cellPhone();
    Integer rentTime = faker.random().nextInt(8);
    String deliveryDate = "2025-01-29";
    String comment = faker.shakespeare().hamletQuote();
    String[] colour;
    String trackId = null;

    @Parameterized.Parameter
    public String[] colours;

    public CreateOrderTest() {
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"WHITE"}},
                {new String[]{"BLACK", "WHITE"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа самоката любого цвета")
    public void checkDifferentColoursForOrder() {
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        Response response = orderSteps.getRequestForCreatingOrder(createOrder);
        trackId = orderSteps.checkResponseAfterCreatingOrder(response);
    }

    @After
    public void cleanData() {
        if (trackId != null) {
            orderSteps.cancelOrder(trackId);
        }
    }
}

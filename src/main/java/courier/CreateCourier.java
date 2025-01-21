package courier;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CreateCourier {
    private String login;
    private String password;
    private String firstName;

}




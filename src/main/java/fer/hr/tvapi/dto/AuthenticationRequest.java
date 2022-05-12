package fer.hr.tvapi.dto;

import fer.hr.tvapi.annotation.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @Email(message = "Invalid email")
    private String email;

    @Size(min = 8 ,max=64, message = "Invalid password")
    private String password;

}

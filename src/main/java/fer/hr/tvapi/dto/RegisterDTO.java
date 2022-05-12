package fer.hr.tvapi.dto;

import fer.hr.tvapi.annotation.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterDTO {

    @Email
    private String email;

    @Size(min = 8, max=64, message = "Invalid password")
    private String password;

}

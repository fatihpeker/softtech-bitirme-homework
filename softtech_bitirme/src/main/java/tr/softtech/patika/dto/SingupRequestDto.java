package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class SingupRequestDto {
    @NotBlank(message = "Username may not be blank")
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank(message = "Password may not be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$",message = "password is not suitable for pattern")
    private String password;
    private String name;
    private String surname;
}

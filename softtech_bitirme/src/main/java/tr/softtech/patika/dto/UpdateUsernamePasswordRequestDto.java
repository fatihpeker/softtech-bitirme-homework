package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateUsernamePasswordRequestDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$",message = "password is not suitable for pattern")
    private String password;
}

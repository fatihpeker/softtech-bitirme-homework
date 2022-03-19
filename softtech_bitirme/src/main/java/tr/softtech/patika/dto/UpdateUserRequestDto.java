package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequestDto {
    private String name;
    private String surname;
}

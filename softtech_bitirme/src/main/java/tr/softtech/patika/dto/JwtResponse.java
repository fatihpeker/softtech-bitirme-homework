package tr.softtech.patika.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class JwtResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private String id;

    private String username;

}

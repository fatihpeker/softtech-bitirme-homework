package tr.softtech.patika.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class UserDto {
    private String userId;
    private String username;
    private String name;
    private String surname;
    private Date createDate;
    private Date updateDate;
    private String createdBy;
    private String updatedBy;
}

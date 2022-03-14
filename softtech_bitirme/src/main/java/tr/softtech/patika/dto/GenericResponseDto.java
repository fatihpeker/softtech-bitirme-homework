package tr.softtech.patika.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponseDto<T> implements Serializable {

    private T data;
    private Date responseDate;
    private boolean isSuccess;
    private String messages;

    public GenericResponseDto(T data, boolean isSuccess,String messages) {
        this.data = data;
        this.isSuccess = isSuccess;
        this.messages = messages;
        responseDate = new Date();
    }

    public static <T> GenericResponseDto<T> of(T t,String messages){
        return new GenericResponseDto<>(t, true,messages );
    }

    public static <T> GenericResponseDto<T> error(T t,String messages){
        return new GenericResponseDto<>(t, false, messages);
    }

    public static <T> GenericResponseDto<T> empty(){
        return new GenericResponseDto<>(null, true, empty().getMessages());
    }

}

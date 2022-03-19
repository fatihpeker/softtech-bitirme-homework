package tr.softtech.patika.converter;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tr.softtech.patika.dto.SingupRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User singupRequestDtoToUser(SingupRequestDto singupRequestDto);

    UserDto userToUserDto(User user);
}

package tr.softtech.patika.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tr.softtech.patika.converter.UserMapper;
import tr.softtech.patika.dto.UpdateUserRequestDto;
import tr.softtech.patika.dto.UpdateUsernamePasswordRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.exception.UsernameAlreadyExistException;
import tr.softtech.patika.model.User;
import tr.softtech.patika.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BaseEntityFieldService baseEntityFieldService;

    public UserDto getUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username).orElseThrow(()-> new ItemNotFoundException("User not found"));
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public UserDto updateUserProperties(UpdateUserRequestDto updateUserRequestDto){
        User user = getUserForInside();
        user.setName(updateUserRequestDto.getName());
        user.setSurname(updateUserRequestDto.getSurname());
        baseEntityFieldService.addBaseEntityProperties(user);
        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    public UserDto updateUsernamePassword(UpdateUsernamePasswordRequestDto updateUsernamePasswordRequestDto){
        if (isUsernameExist(updateUsernamePasswordRequestDto.getUsername())){
            throw new UsernameAlreadyExistException("Username already exist");
        }
        User user = getUserForInside();
        user.setUsername(updateUsernamePasswordRequestDto.getUsername());
        user.setPassword(updateUsernamePasswordRequestDto.getPassword());
        baseEntityFieldService.addBaseEntityFieldWithoutContextHolder(user, user.getUsername());
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public void deleteUser(){
        User user = getUserForInside();
        userRepository.deleteById(user.getUserId());
    }

    //Security kullanımı için
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(()-> new ItemNotFoundException("User not found"));
    }

    public User getUserForInside(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByUsername(username).orElseThrow(()-> new ItemNotFoundException("User not found"));
    }

    public boolean isUsernameExist(String username){
        return userRepository.existsByUsername(username);
    }

}

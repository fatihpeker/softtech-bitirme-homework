package tr.softtech.patika.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tr.softtech.patika.dto.UpdateUserRequestDto;
import tr.softtech.patika.dto.UpdateUsernamePasswordRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.exception.ItemNotFoundException;
import tr.softtech.patika.exception.UsernameAlreadyExistException;
import tr.softtech.patika.model.User;
import tr.softtech.patika.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {


    private Authentication authentication;
    private UserRepository userRepository;
    private BaseEntityFieldService baseEntityFieldService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        authentication = Mockito.mock(Authentication.class);
        userRepository = Mockito.mock(UserRepository.class);
        baseEntityFieldService = Mockito.mock(BaseEntityFieldService.class);

        userService = new UserService(userRepository,baseEntityFieldService);

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }


    @Test
    public void testGetUser_whenUsernameNotExist_shouldThrowItemNotFoundException(){
        String username = "anonymousUser";

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class,() ->userService.getUser());

        Mockito.verify(authentication).getName();
    }

    @Test
    public void testGetUser_whenUserNotExist_shouldThrowItemNotFoundException(){
        String username = getUsername();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class,() ->userService.getUser());

        Mockito.verify(authentication).getName();
    }

    @Test
    public void testGetUser_whenUserAndUsernameExist_shouldReturnUserDto(){
        String username = getUsername();
        User user = getUserObject();
        UserDto expectedResult = getUserDtoObject();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(user));

        UserDto result = userService.getUser();

        assertEquals(expectedResult,result);

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);
    }

    @Test
    public void testUpdateUserProperties_whenUserNotExist_shouldThrowItemNotFoundException(){
        String username = "anonymousUser";
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .name("name")
                .surname("surname")
                .build();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class,
                () -> userService.updateUserProperties(updateUserRequestDto));

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);

    }

    @Test
    public void testUpdateUserProperties_whenUserExist_shouldReturnUserDto(){
        String username = getUsername();
        User user = getUserObject();
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
                .name("name")
                .surname("surname")
                .build();
        UserDto expectedUserDto = getUserDtoObject();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.updateUserProperties(updateUserRequestDto);

        assertEquals(expectedUserDto,result);

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);
        Mockito.verify(baseEntityFieldService).addBaseEntityProperties(user);
    }

    @Test
    public void testUpdateUserNamePassword_whenUsernameAlreadyExist_shouldThrowUsernameAlreadyExistException(){
        UpdateUsernamePasswordRequestDto updateUsernamePasswordRequestDto = UpdateUsernamePasswordRequestDto.builder()
                .username("username")
                .password("password")
                .build();

        Mockito.when(userRepository.existsByUsername(updateUsernamePasswordRequestDto.getUsername()))
                .thenThrow(UsernameAlreadyExistException.class);

        assertThrows(UsernameAlreadyExistException.class,
                ()->userService.updateUsernamePassword(updateUsernamePasswordRequestDto));

        Mockito.verify(userRepository).existsByUsername(updateUsernamePasswordRequestDto.getUsername());
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testUpdateUserNamePassword_whenUsernameIsValid_shouldReturnUserDto(){
        UpdateUsernamePasswordRequestDto updateUsernamePasswordRequestDto = UpdateUsernamePasswordRequestDto.builder()
                .username("username")
                .password("password")
                .build();
        UserDto expectedResult = getUserDtoObject();
        User user = getUserObject();
        String username = getUsername();

        Mockito.when(userRepository.existsByUsername(updateUsernamePasswordRequestDto.getUsername())).thenReturn(false);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(user));

        UserDto result = userService.updateUsernamePassword(updateUsernamePasswordRequestDto);

        assertEquals(expectedResult,result);


        Mockito.verify(userRepository).existsByUsername(updateUsernamePasswordRequestDto.getUsername());
        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);
        Mockito.verify(baseEntityFieldService).addBaseEntityFieldWithoutContextHolder(user,user.getUsername());
    }

    @Test
    public void testDeleteUser_whenUserNotExist_shouldThrowItemNotFoundException(){
        String username = getUsername();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, ()-> userService.deleteUser());

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);
    }

    @Test
    public void testDeleteUser_whenUserExist_shouldDeleteUser(){
        String username = getUsername();
        User user = getUserObject();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(user));

        userService.deleteUser();

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);
        Mockito.verify(userRepository).deleteById(user.getUserId());
    }

    @Test
    public void testGetUserByUsername_whenUserNotExist_shouldThrowItemNotFoundException(){
        String username = getUsername();

        Mockito.when(userRepository.findUserByUsername(username)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class,()-> userService.getUserByUsername(username));

        Mockito.verify(userRepository).findUserByUsername(username);

    }

    @Test
    public void testGetUserByUsername_whenUserExist_shouldReturnUser(){
        String username = getUsername();
        User user = getUserObject();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(user));

        User result = userService.getUserByUsername(username);

        assertEquals(user,result);

        Mockito.verify(userRepository).findUserByUsername(username);

    }

    @Test
    public void testGetUserForInside_whenUserNotExist_shouldThrowItemNotFoundException() {
        String username = "anonymousUser";

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenThrow(ItemNotFoundException.class);

        assertThrows(ItemNotFoundException.class, () -> userService.getUserForInside());

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);
    }

    @Test
    public void testGetUserForInside_whenUserExist_shouldReturnUser(){
        String username = getUsername();
        User user = getUserObject();

        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.ofNullable(user));

        User result = userService.getUserForInside();

        assertEquals(user,result);

        Mockito.verify(authentication).getName();
        Mockito.verify(userRepository).findUserByUsername(username);

    }

    private String getUsername() {
        return "username";
    }

    private UserDto getUserDtoObject() {
        return UserDto.builder()
                .userId("id")
                .username("username")
                .name("name")
                .surname("surname")
                .createDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .updateDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .createdBy("createdBy")
                .updatedBy("updatedBy")
                .build();
    }

    private User getUserObject() {
        return User.builder()
                .userId("id")
                .username("username")
                .password("password")
                .name("name")
                .surname("surname")
                .createDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .updateDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .createdBy("createdBy")
                .updatedBy("updatedBy")
                .build();
    }


}
package tr.softtech.patika.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.softtech.patika.controller.AuthController;
import tr.softtech.patika.dto.JwtResponse;
import tr.softtech.patika.dto.LoginRequestDto;
import tr.softtech.patika.dto.SingupRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.exception.UsernameAlreadyExistException;
import tr.softtech.patika.model.User;
import tr.softtech.patika.repository.UserRepository;
import tr.softtech.patika.security.JwtUtils;
import tr.softtech.patika.security.MyUserDetails;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private BaseEntityFieldService baseEntityFieldService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtUtils = Mockito.mock(JwtUtils.class);
        baseEntityFieldService = Mockito.mock(BaseEntityFieldService.class);

        authService = new AuthService(userRepository,encoder,authenticationManager,jwtUtils,baseEntityFieldService);
    }

    @Test
    public void testSingUp_whenUsernameAlreadyExist_shouldThrowUsernameAlreadyExistException(){
        SingupRequestDto singupRequestDto = SingupRequestDto.builder()
                .username("username")
                .password("password")
                .name("name")
                .surname("surname")
                .build();

        Mockito.when(userRepository.existsByUsername(singupRequestDto.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistException.class, ()-> authService.singup(singupRequestDto));

        Mockito.verify(userRepository).existsByUsername(singupRequestDto.getUsername());
        Mockito.verifyNoInteractions(encoder);
        Mockito.verifyNoInteractions(baseEntityFieldService);
    }

    @Test
    public void testSingUp_whenSingUpRequestDtoIsValid_shouldReturnMappingJacksonValue(){
        SingupRequestDto singupRequestDto = SingupRequestDto.builder()
                .username("username")
                .password("P4ssword")
                .name("name")
                .surname("surname")
                .build();
        User user = getUserObject();
        UserDto userDto = getUserDtoObject();

        Mockito.when(userRepository.existsByUsername(singupRequestDto.getUsername())).thenReturn(false);
        Mockito.when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        WebMvcLinkBuilder singinLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        AuthController.class).authenticateUser(new LoginRequestDto()));
        EntityModel entityModel = EntityModel.of(userDto);
        entityModel.add(singinLink.withRel("singin"));
        MappingJacksonValue expectedResult = new MappingJacksonValue(entityModel);

        MappingJacksonValue result = authService.singup(singupRequestDto);

        assertEquals(expectedResult.getValue(),result.getValue());

        Mockito.verify(userRepository).existsByUsername(singupRequestDto.getUsername());
        Mockito.verify(encoder).encode(user.getPassword());
        Mockito.verify(baseEntityFieldService).addBaseEntityFieldWithoutContextHolder(any(User.class),anyString());
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    public void testSingin_whenLoginRequestDtoIsAuthenticate_shouldReturnJwtResponse(){
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("username");
        loginRequestDto.setPassword("password");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(new MyUserDetails("id","username","password"));
        String jwt = "jwt";
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        JwtResponse exceptedResult = JwtResponse
                .builder()
                .token(jwt)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .build();

        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
                .thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);

        JwtResponse result = authService.singin(loginRequestDto);

        assertEquals(exceptedResult,result);

        Mockito.verify(authenticationManager).authenticate(usernamePasswordAuthenticationToken);
        Mockito.verify(jwtUtils).generateJwtToken(authentication);
    }

    //TODO Method threw 'org.mockito.exceptions.misusing.UnfinishedStubbingException' exception.
    // Cannot evaluate org.springframework.security.authentication.AuthenticationManager$MockitoMock$175150452.toString()
//    @Test
//    public void testSingin_whenLoginRequestDtoNotAuthenticate_shouldThrowAuthenticationException(){
//        LoginRequestDto loginRequestDto = new LoginRequestDto();
//        loginRequestDto.setUsername("username");
//        loginRequestDto.setPassword("password");
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());
//
//
//        Mockito.when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
//                .thenThrow(AuthenticationException.class);
//
//        assertThrows(AuthenticationException.class, ()-> authService.singin(loginRequestDto));
//
//        Mockito.verify(authenticationManager).authenticate(usernamePasswordAuthenticationToken);
//        Mockito.verifyNoInteractions(jwtUtils);
//    }


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
                .password("P4ssword")
                .name("name")
                .surname("surname")
                .createDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .updateDate(Timestamp.from(Instant.parse("2020-01-01T17:10:10Z")))
                .createdBy("createdBy")
                .updatedBy("updatedBy")
                .build();
    }

}
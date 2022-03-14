package tr.softtech.patika.service;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tr.softtech.patika.controller.AuthController;
import tr.softtech.patika.converter.UserMapper;
import tr.softtech.patika.dto.JwtResponse;
import tr.softtech.patika.dto.LoginRequestDto;
import tr.softtech.patika.dto.SingupRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.exception.UsernameAlreadyExistException;
import tr.softtech.patika.model.User;
import tr.softtech.patika.repository.UserRepository;
import tr.softtech.patika.security.JwtUtils;
import tr.softtech.patika.security.MyUserDetails;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final BaseEntityFieldService baseEntityFieldService;


    public MappingJacksonValue singup(SingupRequestDto singupRequestDto){
        if (isUsernameExist(singupRequestDto.getUsername())){
            throw new UsernameAlreadyExistException("Username already exist");
        }

        User user = UserMapper.INSTANCE.singupRequestDtoToUser(singupRequestDto);
        user.setPassword(encoder.encode(user.getPassword()));//pasword encode şekle getirmek için

        //base entity properties eklemek için
        baseEntityFieldService.addBaseEntityFieldWithoutContextHolder(user, user.getUsername());

        UserDto userDto = UserMapper.INSTANCE.userToUserDto(userRepository.save(user));

        /**
         * hateoas ile singin linki göndermek için
         */
        WebMvcLinkBuilder singinLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        AuthController.class).authenticateUser(new LoginRequestDto()));
        EntityModel entityModel = EntityModel.of(userDto);
        entityModel.add(singinLink.withRel("singin"));
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(entityModel);

        return mappingJacksonValue;
    }


    public JwtResponse singin(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return JwtResponse
                .builder()
                .token(jwt)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .build();
    }

    public boolean isUsernameExist(String username){
        return userRepository.existsByUsername(username);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }
}

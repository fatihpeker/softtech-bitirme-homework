package tr.softtech.patika.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.softtech.patika.dto.GenericResponseDto;
import tr.softtech.patika.dto.JwtResponse;
import tr.softtech.patika.dto.LoginRequestDto;
import tr.softtech.patika.dto.SingupRequestDto;
import tr.softtech.patika.service.AuthService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(tags = "User singup")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SingupRequestDto singupRequestDto) {
        return new ResponseEntity<>(GenericResponseDto.of(authService.singup(singupRequestDto),"User saved successfully")
        , HttpStatus.CREATED);
    }

    @Operation(tags = "User Login",
            description = "User login with your username and password",
            summary = "Password have to fit pattern : \n" +
                    "At least one digit [0-9]\n" +
                    "At least one lowercase character [a-z]\n" +
                    "At least one uppercase character [A-Z]\n" +
                    "At least 8 characters in length, but no more than 32.")
    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(GenericResponseDto.of(authService.singin(loginRequestDto), "Login successful"));
    }
}

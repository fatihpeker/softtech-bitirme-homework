package tr.softtech.patika.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.softtech.patika.dto.GenericResponseDto;
import tr.softtech.patika.dto.UpdateUserRequestDto;
import tr.softtech.patika.dto.UpdateUsernamePasswordRequestDto;
import tr.softtech.patika.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(tags = "Get User")
    @GetMapping()
    public ResponseEntity getUser(){
        return ResponseEntity.ok(GenericResponseDto.of(userService.getUser(),"User properties taken"));
    }

    @Operation(tags = "Update user",description = "Just user properties")
    @PutMapping()
    public ResponseEntity updateUserProperties(@Valid @RequestBody UpdateUserRequestDto updateUserRequestDto){
        return ResponseEntity.ok(GenericResponseDto.of(userService.updateUserProperties(updateUserRequestDto)
                ,"Userproperties updated"));
    }

    //TODO: Auth içinde şifremi unuttum için de bir işlem oluştur
    @Operation(tags = "Update Username-Password")
    @PutMapping("update")
    public ResponseEntity updateUsernamePassword(@Valid @RequestBody UpdateUsernamePasswordRequestDto updateUsernamePasswordRequestDto){
        return ResponseEntity.ok(GenericResponseDto.of(userService.updateUsernamePassword(updateUsernamePasswordRequestDto)
        ,"Username and password updated"));
    }

    @Operation(tags = "Delete User")
    @DeleteMapping()
    public ResponseEntity deleteUser(){
        userService.deleteUser();
        return ResponseEntity.ok(GenericResponseDto.of(Void.TYPE,"User Deleted"));
    }

}

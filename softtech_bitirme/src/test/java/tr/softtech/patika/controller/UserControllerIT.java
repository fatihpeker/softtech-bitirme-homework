package tr.softtech.patika.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.context.SecurityContextHolder;
import tr.softtech.patika.IntegrationTestSupport;
import tr.softtech.patika.dto.UpdateUserRequestDto;
import tr.softtech.patika.dto.UpdateUsernamePasswordRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.model.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends IntegrationTestSupport {

    private final String baseUrl = "http://localhost:8080/api/v1/user";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    public void testGetUser_whenUserAuthenticate_shouldReturnUserDto() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByUsername(username);

        this.mockMvc.perform(get("http://localhost:8080/api/v1/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.userId",is(user.getUserId())))
                .andExpect(jsonPath("$.data.username",is(user.getUsername())))
                .andExpect(jsonPath("$.data.name",is(user.getName())))
                .andExpect(jsonPath("$.data.surname",is(user.getSurname())))
                .andExpect(jsonPath("$.success",is(true)));
    }

    @Test
    public void testUpdateUserProperties_whenUserExist_shouldReturnUserDto() throws Exception {
        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                .name("name")
                .surname("surname")
                .build();

        this.mockMvc.perform(put(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.name",is("name")))
                .andExpect(jsonPath("$.data.surname",is("surname")))
                .andExpect(jsonPath("$.success",is(true)));
    }

    @Test
    public void testUpdateUsernamePassWord_whenUsernameAlreadyExist_shouldNotUpdateAndThrow409CONFLICT() throws Exception {
        User user = getUserObject();
        user.setUsername("AlreadyExist");
        userRepository.save(user);

        UpdateUsernamePasswordRequestDto request = UpdateUsernamePasswordRequestDto.builder()
                .username("AlreadyExist")
                .password("P4ssword")
                .build();

        this.mockMvc.perform(put(baseUrl + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/user/update")))
                .andExpect(jsonPath("$.messages",is("Username already exist")))
                .andExpect(jsonPath("$.success",is(false)));
    }

    @Test
    public void testUpdateUsernamePassWord_whenPasswordIsInvalid_shouldNotUpdateAndThrow400BadRequest() throws Exception {
        UpdateUsernamePasswordRequestDto request = UpdateUsernamePasswordRequestDto.builder()
                .username("user")
                .password("password")
                .build();

        this.mockMvc.perform(put(baseUrl + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/user/update")))
                .andExpect(jsonPath("$.messages",is("password is not suitable for pattern")))
                .andExpect(jsonPath("$.success",is(false)));
    }

    @Test
    public void testUpdateUsernamePassWord_whenUsernameAndPasswordIsValid_shouldUpdateAndReturnUserDto() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByUsername(username);
        UpdateUsernamePasswordRequestDto request = UpdateUsernamePasswordRequestDto.builder()
                .username("user")
                .password("P4ssword")
                .build();

        this.mockMvc.perform(put(baseUrl + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.userId",is(user.getUserId())))
                .andExpect(jsonPath("$.data.name",is(user.getName())))
                .andExpect(jsonPath("$.data.surname",is(user.getSurname())))
                .andExpect(jsonPath("$.messages",is("Username and password updated")))
                .andExpect(jsonPath("$.success",is(true)));
    }

    @Test
    public void testDeleteUser_whenUserAuthenticate_shouldDeleteUser() throws Exception {

        this.mockMvc.perform(delete(baseUrl).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data",is("void")))
                .andExpect(jsonPath("$.messages",is("User Deleted")))
                .andExpect(jsonPath("$.success",is(true)));

        List<User> userList = userRepository.findAll();
        assertEquals(0,userList.size());
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
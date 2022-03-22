package tr.softtech.patika.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tr.softtech.patika.IntegrationTestSupport;
import tr.softtech.patika.dto.LoginRequestDto;
import tr.softtech.patika.dto.SingupRequestDto;
import tr.softtech.patika.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIT extends IntegrationTestSupport {

    private final String baseUrl = "http://localhost:8080/api/v1/auth";

    @Test
    public void testSingup_whenUsernameAlreadyExist_shouldThrow409UsernameAlreadyExistException() throws Exception {
        SingupRequestDto request = getSingupRequestDto();

        this.mockMvc.perform(post(baseUrl + "/signup").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/auth/signup")))
                .andExpect(jsonPath("$.data.message",is("Username already exist")))
                .andExpect(jsonPath("$.success",is(false)));

        List<User> userList = userRepository.findAll();
        assertEquals(1,userList.size());
    }

    @Test
    public void testSingup_whenPasswordIsInvalid_shouldThrow400BadRequest() throws Exception {
        SingupRequestDto request = getSingupRequestDto();
        request.setUsername("user");
        request.setPassword("password");

        this.mockMvc.perform(post(baseUrl + "/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.data.description",is("uri=/api/v1/auth/signup")))
                .andExpect(jsonPath("$.data.message",is("password is not suitable for pattern")))
                .andExpect(jsonPath("$.success",is(false)));

        List<User> userList = userRepository.findAll();
        assertEquals(1,userList.size());
    }

    @Test
    public void testSingup_whenSingupRequestDtoIsValid_shouldReturnMappingJacksonValue() throws Exception {
        SingupRequestDto request = getSingupRequestDto();
        request.setUsername("user");
        request.setPassword("P4ssword");

        this.mockMvc.perform(post(baseUrl + "/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.value.username",is(request.getUsername())))
                .andExpect(jsonPath("$.data.value.name",is(request.getName())))
                .andExpect(jsonPath("$.messages",is("User saved successfully")))
                .andExpect(jsonPath("$.success",is(true)));


        List<User> userList = userRepository.findAll();
        assertEquals(2,userList.size());
    }

    @Test
    public void testSingin_whenUsernameAndPasswordIsInValid_shouldThrow500InternalServerError() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("anonym");
        loginRequestDto.setPassword("P4ssword");

        this.mockMvc.perform(post(baseUrl + "/signin").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(loginRequestDto)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.success",is(false)));
    }

    @Test
    public void testSingin_whenUsernameAndPasswordIsValid_shouldReturnJwtResponse() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("username");
        loginRequestDto.setPassword("P4ssword");

        this.mockMvc.perform(post(baseUrl + "/signin").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(loginRequestDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.data.username",is(loginRequestDto.getUsername())))
                .andExpect(jsonPath("$.success",is(true)));
    }

    private SingupRequestDto getSingupRequestDto() {
        return SingupRequestDto.builder()
                .username("username")
                .password("P4ssword")
                .name("name")
                .surname("surname")
                .build();
    }


}
package tr.softtech.patika.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tr.softtech.patika.IntegrationTestSupport;
import tr.softtech.patika.dto.UpdateUserRequestDto;
import tr.softtech.patika.dto.UserDto;
import tr.softtech.patika.model.User;

import java.sql.Timestamp;
import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends IntegrationTestSupport {

    private final String baseUrl = "http://localhost:8080/api/v1/user";

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Test
//    public void testGetUser_whenUserAuthenticate_shouldReturnUserDto() throws Exception {
//        User user = getUserObject();
//        user = userRepository.save(user);
//
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
////
////        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//        this.mockMvc.perform(get("http://localhost:8080/api/v1/user").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.userId",is(user.getUserId())))
//                .andExpect(jsonPath("$.username",is(user.getUsername())))
//                .andExpect(jsonPath("$.name",is(user.getName())))
//                .andExpect(jsonPath("$.surname",is(user.getSurname())));
//    }

//    @Test
//    public void testUpdateUserProperties_whenUserExist_shouldReturnUserDto() throws Exception {
//        User user = getUserObject();
//        userRepository.save(user);
//        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
//                .name("name")
//                .surname("surname")
//                .build();
//
//        this.mockMvc.perform(put(baseUrl)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.name",is("name")))
//                .andExpect(jsonPath("$.surname",is("surname")));
//
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
package tr.softtech.patika;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tr.softtech.patika.dto.LoginRequestDto;
import tr.softtech.patika.dto.SingupRequestDto;
import tr.softtech.patika.model.User;
import tr.softtech.patika.repository.CategoryRepository;
import tr.softtech.patika.repository.ProductRepository;
import tr.softtech.patika.repository.UserRepository;
import tr.softtech.patika.security.JwtUtils;
import tr.softtech.patika.security.MyUserDetails;
import tr.softtech.patika.service.AuthService;
import tr.softtech.patika.service.CategoryService;
import tr.softtech.patika.service.ProductService;
import tr.softtech.patika.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Context ayağa kaldırır
@TestPropertySource(locations = "classpath:application.yml") // test için kullanılacak propertyleri ayarlar
@DirtiesContext
@AutoConfigureMockMvc // Context içindeki servletleri ayaga kaldırır
public class IntegrationTestSupport {

    @Autowired
    public AuthService authService;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public ProductService productService;

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    public JwtUtils jwtUtils;

    @Autowired
    public PasswordEncoder encoder;


    @Autowired
    private AuthenticationManager authenticationManager;

    public final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    public void setup(){
        /**
         * Kullanıcı SecurityContextHolder.getContext.getAuthentication dan aldığımızdan dolayı
         * Bazı fonksiyonların çalışması bir authenticate işlemi olmalı
         * Aşağıda bir user oluşturup onu authenticate eden kod parçacığı var
         */
        User user = getUser();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        LoginRequestDto loginRequestDto = getLoginRequestDto();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwtUtils.generateJwtToken(authentication);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);

    }

//    private void login(LoginRequestDto loginRequestDto) throws Exception {
//        this.mockMvc.perform(post("http://localhost:8080/api/v1/auth/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(loginRequestDto)));
//    }


    private User getUser() {
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

    private LoginRequestDto getLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("username");
        loginRequestDto.setPassword("P4ssword");
        return loginRequestDto;
    }


}

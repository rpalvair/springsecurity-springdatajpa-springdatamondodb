package com.palvair;


import com.palvair.jpa.UserRepository;
import com.palvair.security.model.User;
import com.palvair.security.model.UserRole;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationConfig.class)
@WebIntegrationTest
public class ApplicationIT {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${server.port:8080}")
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testToken() {
        final HttpHeaders httpHeaders = getTokenFor(new User());
        System.out.println("headers = " + httpHeaders);
    }

    @Test
    public void testUserCurrent() {
        final HttpHeaders httpHeaders = getTokenFor(new User());
        final HttpEntity<String> testRequest = new HttpEntity<>(null, httpHeaders);
        final String url = buildUrl("api/users/current");
        final ResponseEntity<User> testResponse = restTemplate.exchange(url, HttpMethod.GET, testRequest,
                User.class);
        System.out.println("testResponse = " + testResponse);
        Assert.assertEquals(HttpStatus.OK, testResponse.getStatusCode());
        final User user = testResponse.getBody();
        Assert.assertNotNull(user);
    }

    private HttpHeaders getTokenFor(final User user) {
        //TODO : to json
        //TODO : add in HttpEntity
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final String password = "admin";
        final String username = "admin";
        final HttpEntity<String> login = new HttpEntity<>(
                "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}", httpHeaders);
        final String url = buildUrl("api/login");
        ResponseEntity<Void> results = restTemplate.postForEntity(url, login, Void.class);
        Assert.assertEquals(HttpStatus.OK, results.getStatusCode());
        Assert.assertNotNull(results.getHeaders().getFirst("X-AUTH-TOKEN"));
        httpHeaders.add("X-AUTH-TOKEN", results.getHeaders().getFirst("X-AUTH-TOKEN"));
        return httpHeaders;
    }

    private String buildUrl(final String path) {
        return UriComponentsBuilder.newInstance()
                .host("localhost")
                .scheme("http")
                .path(path)
                .port(port)
                .build()
                .toString();
    }

    @Before
    public void before() {
        userRepository.deleteAll();
        addUser("admin", "admin");
        addUser("user", "user");
    }

    @After
    public void after() {
        userRepository.deleteAll();
    }

    private void addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.grantRole(username.equals("admin") ? UserRole.ADMIN : UserRole.USER);
        System.out.println("save user = " + user);
        userRepository.save(user);
    }

}

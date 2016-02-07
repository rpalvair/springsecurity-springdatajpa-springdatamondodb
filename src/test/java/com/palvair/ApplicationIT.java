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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Created by widdy on 20/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationConfig.class)
@WebIntegrationTest
public class ApplicationIT {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testToken() {
        HttpHeaders httpHeaders = getToken();
        System.out.println("headers = " + httpHeaders);
    }

    @Test
    public void testUserCurrent() {
        HttpHeaders httpHeaders = getToken();
        HttpEntity<String> testRequest = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<User> testResponse = restTemplate.exchange("http://localhost:8080/api/users/current", HttpMethod.GET, testRequest,
                User.class);
        System.out.println("testResponse = " + testResponse);
        Assert.assertEquals(HttpStatus.OK, testResponse.getStatusCode());
        final User user = testResponse.getBody();
        Assert.assertNotNull(user);
    }

    private HttpHeaders getToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String password = "admin";
        String username = "admin";
        HttpEntity<String> login = new HttpEntity<>(
                "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}", httpHeaders);
        ResponseEntity<Void> results = restTemplate.postForEntity("http://localhost:8080/api/login", login, Void.class);
        Assert.assertEquals(HttpStatus.OK, results.getStatusCode());
        Assert.assertNotNull(results.getHeaders().getFirst("X-AUTH-TOKEN"));
        httpHeaders.add("X-AUTH-TOKEN", results.getHeaders().getFirst("X-AUTH-TOKEN"));
        return httpHeaders;
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

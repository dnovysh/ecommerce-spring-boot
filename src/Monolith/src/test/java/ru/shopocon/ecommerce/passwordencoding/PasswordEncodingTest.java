package ru.shopocon.ecommerce.passwordencoding;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordEncodingTest {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @TestFactory
    Stream<DynamicTest> testDemoUserCredentials() {
        final var demoUserCredentials = List.of(
            new userNamePasswordTuple("shopocon1@shopocon.com", "shopocon"),
            new userNamePasswordTuple("shopocon2@shopocon.com", "eShopOnContainer"),
            new userNamePasswordTuple("stpete@stpete.com", "password"),
            new userNamePasswordTuple("dunedin@dunedin.com", "password"),
            new userNamePasswordTuple("keywest@keywest.com", "password"),
            new userNamePasswordTuple("primeng@primeng.com", "password"),
            new userNamePasswordTuple("scott@oracle.com", "tiger"),
            new userNamePasswordTuple("user@example.com", "password"),
            new userNamePasswordTuple("buyer@example.com", "shopper")
        );
        return demoUserCredentials.stream().map(userNamePassword -> DynamicTest.dynamicTest(
            "User { %s } has password { %s }".formatted(userNamePassword.username, userNamePassword.password),
            () -> assertTrue(passwordEncoder.matches(userNamePassword.password,
                userDetailsService.loadUserByUsername(userNamePassword.username).getPassword()))));
    }

    @Test
    void testBcrypt() {
        int strength = 12;
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(strength);
        System.out.println(bcrypt.encode("shopocon"));
        assertTrue(bcrypt.matches("shopocon",
            "$2a$12$GKckgltLA4Vcp8sn1NF5Quhv8wPSJWQ4X1Ls4dLBQSQvA8M8eAMb6"));
    }

    private record userNamePasswordTuple(String username, String password) {
    }
}

package ru.shopocon.ecommerce;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTest {

    @Test
    void testBcrypt() {
        int strength = 12;
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(strength);

        System.out.println(bcrypt.encode("shopocon"));
        System.out.println(bcrypt.encode("eShopOnContainer"));
        System.out.println(bcrypt.encode("password"));
        System.out.println(bcrypt.encode("tiger"));
        System.out.println(bcrypt.encode("shopper"));

        assertTrue(bcrypt.matches("shopocon",
            "$2a$12$GKckgltLA4Vcp8sn1NF5Quhv8wPSJWQ4X1Ls4dLBQSQvA8M8eAMb6"));
    }
}

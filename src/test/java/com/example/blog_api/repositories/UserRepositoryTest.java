package com.example.blog_api.repositories;


import com.example.blog_api.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository underTest;

//    @AfterEach
//    void tearDown() {
//        underTest.deleteAll();
//    }


    @Test
    void itShouldFindUserByEmail() {
        String email = "obid@yahoo.com";
        User user = new User();
        user.setFirstName("Chisom");
        user.setLastName("Obidike");
        user.setEmail(email);
        user.setPassword("1234");
        underTest.save(user);

        User foundUser = underTest.findByEmail(email);

        assertThat(foundUser).isEqualTo(user);
    }


    @Test
    void shouldNotFindUserWhenEmailDoesNotExist(){
        String email = "obid@yahoo.com";
        User foundUser = underTest.findByEmail(email);

        assertThat(foundUser).isNull();
    }
}
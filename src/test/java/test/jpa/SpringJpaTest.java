package test.jpa;

import com.xxq.jpa.Application;
import com.xxq.jpa.domain.User;
import com.xxq.jpa.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class SpringJpaTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void test() {
        userRepository.save(new User());
        userRepository.save(new User());
        System.out.println(userRepository.findAll());
    }
}

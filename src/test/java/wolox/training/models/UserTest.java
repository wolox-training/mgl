package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1));
        Optional<User> opt = Optional.of(user);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(opt);
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        String username = "mary";
        Optional<User> found = userRepository.findByUsername(username);

        assertThat(found.get().getUsername())
            .isEqualTo(username);
    }

    @Test
    public void whenInitializeUserWithoutUsername_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User(null, "Mary Lewis", LocalDate.of(1990, 1, 1));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("", "Mary Lewis", LocalDate.of(1990, 1, 1));
        });
    }

    @Test
    public void whenInitializeUserWithoutName_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", null, LocalDate.of(1990, 1, 1));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "", LocalDate.of(1990, 1, 1));
        });
    }

    @Test
    public void whenInitializeUserWithoutBirthDate_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "Mary Lewis", null);
        });
    }

    @Test
    public void whenInitializeUserWithWrongBirthDate_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "Mary Lewis", LocalDate.now());
        });
    }
}

package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByUsername_thenReturnUser() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsername(user.getUsername());

        assertThat(found.get().getUsername())
            .isEqualTo(user.getUsername());
    }

    @Test
    public void whenFindByBirthDateBetweenAndNameIgnoreCase_thenReturnUser() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        User fakeUser = new User("mary", "Mary Lewis", LocalDate.of(1993, 1, 1), "lewis");

        entityManager.persist(user);
        entityManager.persist(fakeUser);
        entityManager.flush();

        List<User> found = userRepository
            .findByBirthDateBetweenAndNameIgnoreCase(LocalDate.of(1980, 1, 1),
                LocalDate.of(1991, 1, 1), "marY lewiS");

        assertThat(found).isEqualTo(Arrays.asList(user));
    }

    @Test
    public void whenFindByBirthDateBetween_thenReturnUser() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        User fakeUser = new User("mary", "Mary Lewis", LocalDate.of(1993, 1, 1), "lewis");

        entityManager.persist(user);
        entityManager.persist(fakeUser);
        entityManager.flush();

        List<User> found = userRepository
            .findByBirthDateBetweenAndNameIgnoreCase(LocalDate.of(1980, 1, 1),
                LocalDate.of(1991, 1, 1), null);

        assertThat(found).isEqualTo(Arrays.asList(user));
    }

    @Test
    public void whenFindByBirthDateStart_thenReturnUser() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        User fakeUser = new User("mary", "Mary Lewis", LocalDate.of(1993, 1, 1), "lewis");

        entityManager.persist(user);
        entityManager.persist(fakeUser);
        entityManager.flush();

        List<User> found = userRepository
            .findByBirthDateBetweenAndNameIgnoreCase(LocalDate.of(1980, 1, 1),
                null, null);

        assertThat(found).isEqualTo(Arrays.asList(user, fakeUser));
    }

    @Test
    public void whenFindByBirthDateEnd_thenReturnUser() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        User fakeUser = new User("mary", "Mary Lewis", LocalDate.of(1993, 1, 1), "lewis");

        entityManager.persist(user);
        entityManager.persist(fakeUser);
        entityManager.flush();

        List<User> found = userRepository
            .findByBirthDateBetweenAndNameIgnoreCase(null,
                LocalDate.of(1991, 1, 1), null);

        assertThat(found).isEqualTo(Arrays.asList(user));
    }

    @Test
    public void whenFindByNameIgnoreCase_thenReturnUser() {
        User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        User fakeUser = new User("mary", "Mary Lewis", LocalDate.of(1993, 1, 1), "lewis");

        entityManager.persist(user);
        entityManager.persist(fakeUser);
        entityManager.flush();

        List<User> found = userRepository
            .findByBirthDateBetweenAndNameIgnoreCase(null, null, "marY lewiS");

        assertThat(found).isEqualTo(Arrays.asList(user, fakeUser));
    }

    @Test
    public void whenInitializeUserWithoutUsername_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User(null, "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("", "Mary Lewis", LocalDate.of(1990, 1, 1), "lewis");
        });
    }

    @Test
    public void whenInitializeUserWithoutName_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", null, LocalDate.of(1990, 1, 1), "lewis");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "", LocalDate.of(1990, 1, 1), "lewis");
        });
    }

    @Test
    public void whenInitializeUserWithoutBirthDate_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "Mary Lewis", null, "lewis");
        });
    }

    @Test
    public void whenInitializeUserWithWrongBirthDate_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "Mary Lewis", LocalDate.now(), "lewis");
        });
    }

    @Test
    public void whenInitializeUserWithoutPassword_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            User user = new User("mary", "Mary Lewis", LocalDate.of(1990, 1, 1), "");
        });
    }
}

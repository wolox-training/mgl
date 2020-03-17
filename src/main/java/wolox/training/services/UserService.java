package wolox.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    public User updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));

        return repository.save(user);
    }
}

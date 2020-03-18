package wolox.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(User user, User modifiedUser) {
        user.setUsername(modifiedUser.getUsername());
        user.setName(modifiedUser.getName());
        user.setBirthDate(modifiedUser.getBirthDate());

        return userRepository.save(user);
    }

    public User updatePassword(User user, String password) {
        user.setPassword(password);

        return userRepository.save(user);
    }
}

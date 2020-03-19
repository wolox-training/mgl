package wolox.training.security;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;
import wolox.training.services.PasswordEncoderService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> user = userRepository.findByUsername(name);

        if (user.isPresent() && passwordEncoderService
            .matches(password, user.get().getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                name, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

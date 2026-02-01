package netoTests.usersTests.login;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.LoginUserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginUserTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    LoginUserService service;

    @Test
    void testLoginUserWithValidCredentials() {
        String email = "user@gmail.com";
        String password = "Senh@123";

        User user = new User(
                email,
                password,
                password,
                "Jay",
                "Purple",
                "12345678900",
                "2000-01-01"
        );

        when(userRepository.findByEmailAndPassword(email, password))
                .thenReturn(user);

        User loggedUser = service.login(email, password);

        assertNotNull(loggedUser);
        assertEquals(email, loggedUser.getEmail());

        verify(userRepository, times(1))
                .findByEmailAndPassword(email, password);
    }

    @Test
    void testLoginUserWithEmptyEmailField() {
        String email = "";
        String password = "Senh@123";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.login(email, password));

        assertEquals("Email cannot be empty", exception.getMessage());

        verify(userRepository, never())
                .findByEmailAndPassword(anyString(), anyString());
    }

    @Test
    void testLoginUserWithEmptyPasswordField() {
        String email = "user@gmail.com";
        String password = "";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.login(email, password));

        assertEquals("Password cannot be empty", exception.getMessage());

        verify(userRepository, never())
                .findByEmailAndPassword(anyString(), anyString());
    }

    @Test
    void testNotLoginWhenPasswordIsIncorrect() {
        String email = "user@gmail.com";
        String wrongPassword = "SENHA";

        when(userRepository.findByEmailAndPassword(email, wrongPassword))
                .thenReturn(null);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.login(email, wrongPassword));

        assertEquals("Invalid email or password", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmailAndPassword(email, wrongPassword);
    }

    @Test
    void testNotLoginWhenEmailIsIncorrect() {
        String wrongEmail = "userErrado@gmail.com";
        String password = "Senh@123";

        when(userRepository.findByEmailAndPassword(wrongEmail, password))
                .thenReturn(null);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.login(wrongEmail, password));

        assertEquals("Invalid email or password", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmailAndPassword(wrongEmail, password);
    }
}

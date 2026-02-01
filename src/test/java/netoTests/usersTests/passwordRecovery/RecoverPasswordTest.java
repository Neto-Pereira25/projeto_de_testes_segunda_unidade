package netoTests.usersTests.passwordRecovery;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.RecoverPasswordService;
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
public class RecoverPasswordTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RecoverPasswordService service;

    @Test
    void testRecoverPasswordWhenDataIsValidAndUserExists() {
        String email = "user@gmail.com";
        String newPassword = "Senha@123";
        String confirmNewPassword = "Senha@123";

        User user = new User(
                email,
                "oldPassword",
                "oldPassword",
                "Bob",
                "Brown",
                "12345678900",
                "2000-01-01"
        );

        when(userRepository.findByEmail(email))
                .thenReturn(user);

        User updatedUser = service
                .recover(email, newPassword, confirmNewPassword);

        assertNotNull(updatedUser);
        assertEquals(email, updatedUser.getEmail());

        verify(userRepository, times(1))
                .findByEmail(email);
    }
    
    @Test
    public void testNotRecoveryPasswordWhenEmailDoesNotExist() {
        String unregisteredEmail = "userInexistente@gmail.com";
        String newPassword = "Senha@123";
        String confirmNewPassword = "Senha@123";

        when(userRepository.findByEmail(unregisteredEmail))
                .thenReturn(null);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.recover(unregisteredEmail, newPassword, confirmNewPassword));

        assertEquals("This email address is not registered.", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmail(unregisteredEmail);
    }
    
    @Test
    public void testRecoveryPasswordWithEmptyEmailField() {
        String email = "";
        String newPassword = "Senha@123";
        String confirmNewPassword = "Senha@123";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.recover(email, newPassword, confirmNewPassword));

        assertEquals("Email cannot be empty", exception.getMessage());

        verify(userRepository, never())
                .findByEmail(anyString());
    }
    
    @Test
    public void testRecoveryPasswordWithInvalidEmailField() {
        String email = "email-invalido#gmail.com";
        String newPassword = "Senha@123";
        String confirmNewPassword = "Senha@123";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.recover(email, newPassword, confirmNewPassword));

        assertEquals("Email field is invalid.", exception.getMessage());

        verify(userRepository, never())
                .findByEmail(anyString());
    }
    
    @Test
    public void testRecoveryPasswordWithInvalidPassword() {
        String email = "user@gmail.com";
        String newPassword = "Senha";
        String confirmNewPassword = "Senha@123";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.recover(email, newPassword, confirmNewPassword));

        assertEquals("New Password field is invalid.", exception.getMessage());

        verify(userRepository, never())
                .findByEmail(anyString());
    }
    
    @Test
    public void testRecoveryPasswordWithInvalidPasswordConfirmationFields() {
        String email = "user@gmail.com";
        String newPassword = "Senha@123";
        String confirmNewPassword = "Senha";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.recover(email, newPassword, confirmNewPassword));

        assertEquals("Confirm New Password field is invalid.", exception.getMessage());

        verify(userRepository, never())
                .findByEmail(anyString());
    }
    
    @Test
    public void testRecoveryPasswordWithDifferentPasswordAndPasswordConfirmationFields() {
        String email = "user@gmail.com";
        String newPassword = "Senha@123";
        String confirmNewPassword = "Senha@1234";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.recover(email, newPassword, confirmNewPassword));

        assertEquals("Password and password confirmation must be the same!", exception.getMessage());

        verify(userRepository, never())
                .findByEmail(anyString());
    }
}

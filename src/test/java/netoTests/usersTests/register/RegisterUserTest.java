/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package netoTests.usersTests.register;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.RegisterUserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Neto Pereira
 */
@ExtendWith(MockitoExtension.class)
public class RegisterUserTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RegisterUserService service;

    @Test
    public void testRegisterUserWithValidFields() {
        User user = new User(
                "user@gmail.com",
                "Senh@123",
                "Senh@123",
                "usuario",
                "Silva",
                "12345678910",
                "01/01/1970"
        );

        when(userRepository.saveUser(any(User.class))).thenReturn(user);

        User registeredUser = service.register(
                "user@gmail.com",
                "Senh@123",
                "Senh@123",
                "usuario",
                "Silva",
                "12345678910",
                "01/01/1970"
        );

        assertNotNull(registeredUser);
        assertEquals("usuario", registeredUser.getName());

        System.out.println("Esperado (A): " + user.getName());
        System.out.println("Retornado: " + registeredUser.getName());

        verify(userRepository, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testRegisterUserWithDifferentPasswordAndPasswordConfirmationFields() {
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.register(
                        "user@gmail.com",
                        "Senh@123",
                        "Senh@Diferente123",
                        "usuario",
                        "Silva",
                        "12345678910",
                        "01/01/1970"
                )
        );

        assertEquals(
                "Password and password confirmation must be the same!",
                exception.getMessage()
        );

        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    public void testRegisterUserWithEmptyPasswordConfirmationField() {
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.register(
                        "user@gmail.com",
                        "Senh@123",
                        "",
                        "usuario",
                        "Silva",
                        "12345678910",
                        "01/01/1970"
                )
        );

        assertEquals(
                "Password and password confirmation must be the same!",
                exception.getMessage()
        );

        verify(userRepository, never()).saveUser(any(User.class));
    }
}

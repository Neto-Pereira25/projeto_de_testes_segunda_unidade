/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package netoTests.usersTests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Neto Pereira
 */
@ExtendWith(MockitoExtension.class)
public class RegisterUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUserService service;

    @Test
    public void cadastrarUsuarioComCamposValidos() {
        var email = "exemplo@teste.com";
        var password = "123456";
        var passwordConfirmation = "123456";
        var name = "Bob";
        var surname = "Brown";
        var cpf = "123.456.789-01";
        var birthDate = "10/09/1999";

        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = service.register(
                email, password,
                passwordConfirmation, name,
                surname, cpf,
                birthDate);

        assertNotNull(registeredUser);
        assertEquals("Bob", registeredUser.getName());

        verify(userRepository, times(1)).save(any(User.class));
    }
}

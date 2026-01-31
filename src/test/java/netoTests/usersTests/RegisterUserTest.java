/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package netoTests.usersTests;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.RegisterUserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void testRegisterUserWithValidFields() {
        User user = new User(
                "exemplo@teste.com",
                "Senh@123",
                "Senh@123",
                "Bob",
                "Brown",
                "123.456.789-01",
                "10/09/1999");

        when(userRepository.saveUser(any(User.class))).thenReturn(user);

        User registeredUser = service.register(
                "exemplo@exemplo.com",
                "Senh@123",
                "Senh@123",
                "Dylan",
                "Brown",
                "123.456.789-02",
                "10/09/1999"
        );

        assertNotNull(registeredUser);
        assertEquals("Dylan", registeredUser.getName());

        verify(userRepository, times(1)).saveUser(any(User.class));
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HelenaTest;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.RegisterUserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 *
 * @author helena
 */
@ExtendWith(MockitoExtension.class)
public class CadastroUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Test
    void DeveCadastrarUsuarioDadosValidos() {
        String email = "user@gmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123";
        String nome = "usuario";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        when(userRepository.saveUser(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, User.class));

        User response = registerUserService.register(
                email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
        );

        assertNotNull(response);
        assertEquals(nome, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(sobrenome, response.getSurname());
        assertEquals(cpf, response.getCpf());
        assertEquals(dataNascimento, response.getBirthDate());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).saveUser(captor.capture());

        User salvo = captor.getValue();
        assertEquals(email, salvo.getEmail());
        assertEquals(senha, salvo.getPassword());
        assertEquals(confirmacaoSenha, salvo.getPasswordConfirmation());
        assertEquals(nome, salvo.getName());
        assertEquals(sobrenome, salvo.getSurname());
        assertEquals(cpf, salvo.getCpf());
        assertEquals(dataNascimento, salvo.getBirthDate());

    }

    @Test
    void DeveRecusarCadastroComEmailInvalido() {
        String email = "usergmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123";
        String nome = "usuario";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> registerUserService.register(
                        email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
                )
        );

        assertEquals("Email field is invalid.", ex.getMessage());
        verify(userRepository, never()).saveUser(any());
    }

    @Test
    void DeveRecusarCadastroComEmailVazio() {
        String email = "";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123";
        String nome = "usuario";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> registerUserService.register(
                        email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
                )
        );

        assertEquals("Campo e-mail precisa ser preenchido.", ex.getMessage());
        verify(userRepository, never()).saveUser(any());
    }

    @Test
    void DeveRecusarCadastroComEmailJaCadastrado() {
        String email = "user@gmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123";
        String nome = "usuario";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> registerUserService.register(
                        email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
                )
        );

        assertEquals("O e-mail informado já possui cadastro.", ex.getMessage());
        verify(userRepository, never()).saveUser(any());
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void DeveRecusarCadastroComNomeComCaracteresInvalidos() {
        String email = "user@gmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123";
        String nome = "u$u@ri*";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> registerUserService.register(
                        email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
                )
        );

        assertEquals("Campo nome possui caracteres inválidos.", ex.getMessage());

        verifyNoInteractions(userRepository);
    }

    @Test
    void DeveRecusarCadastroComSobrenomeComCaracteresInvalidos() {
        String email = "user@gmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123"; 
        String nome = "usuario";
        String sobrenome = "silv@"; 
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> registerUserService.register(
                        email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
                )
        );

        assertEquals("Campo sobrenome possui caracteres inválidos.", ex.getMessage());

        verifyNoInteractions(userRepository);
    }

}

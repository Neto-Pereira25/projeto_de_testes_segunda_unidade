/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HelenaTest;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.RegisterUserWithCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author helena
 */
@ExtendWith(MockitoExtension.class)
public class CadastroUserComCodigoTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegisterUserWithCodeService.PendingRegistrationStore pendingStore;

    @Mock
    private RegisterUserWithCodeService.VerificationCodeSender codeSender;

    @Mock
    private RegisterUserWithCodeService.VerificationCodeGenerator codeGenerator;

    @InjectMocks
    private RegisterUserWithCodeService service;

    @Test
    void DeveEnviarCodigoEAguardarConfirmacao() {
        String email = "user@gmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123"; 
        String nome = "usuario";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(codeGenerator.generateCode()).thenReturn("123456");

        String msg = service.solicitarCodigo(
                email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
        );

        assertEquals("Código enviado para o usuário. Aguarde a confirmação.", msg);

        verify(userRepository, never()).saveUser(any(User.class));
        verify(pendingStore).save(eq(email), any(RegisterUserWithCodeService.PendingRegistration.class));
        verify(codeSender).send(email, "123456");
    }

    @Test
    void DeveConfirmarCodigoECadastrarUsuario() {
        String email = "user@gmail.com";
        String code = "123456";

        RegisterUserWithCodeService.PendingRegistration pending =
                new RegisterUserWithCodeService.PendingRegistration(
                email,
                "Senh@123",
                "Senh@123",
                "usuario",
                "silva",
                "12345678910",
                "01/01/1970",
                "123456"
        );

        when(pendingStore.findByEmail(email)).thenReturn(pending);
        when(userRepository.saveUser(any(User.class))).thenAnswer(inv -> inv.getArgument(0, User.class));

        String msg = service.confirmarCodigo(email, code);

        assertEquals("Cadastro confirmado com sucesso.", msg);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveUser(captor.capture());
        assertEquals(email, captor.getValue().getEmail());

        verify(pendingStore).delete(email);
    }
}

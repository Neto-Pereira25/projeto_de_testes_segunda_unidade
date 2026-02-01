/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HelenaTest;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
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
    private PendingRegistrationStore pendingStore;

    @Mock
    private VerificationCodeSender codeSender;

    @Mock
    private VerificationCodeGenerator codeGenerator;

    @InjectMocks
    private RegisterUserWithCodeService service;

    @Test
    void DeveEnviarCodigoEAguardarConfirmacao() {
        String email = "user@gmail.com";
        String senha = "Senh@123";
        String confirmacaoSenha = "Senh@123"; // igual para não cair em mismatch
        String nome = "usuario";
        String sobrenome = "silva";
        String cpf = "12345678910";
        String dataNascimento = "01/01/1970";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(codeGenerator.generateCode()).thenReturn("123456");

        CodeSentResponse response = service.solicitarCodigo(
                email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
        );

        assertTrue(response.sucesso());
        assertEquals("Código enviado para o usuário.", response.mensagem());

        // não salva o usuário ainda
        verify(userRepository, never()).saveUser(any(User.class));

        // salva pendência e envia o código
        ArgumentCaptor<PendingRegistration> captor = ArgumentCaptor.forClass(PendingRegistration.class);
        verify(pendingStore).save(eq(email), captor.capture());
        verify(codeSender).send(email, "123456");

        PendingRegistration pending = captor.getValue();
        assertEquals(email, pending.email());
        assertEquals("123456", pending.code());
        assertEquals(nome, pending.name());
    }

    @Test
    void DeveConfirmarCodigoECadastrarUsuario() {
        String email = "user@gmail.com";
        String code = "123456";

        PendingRegistration pending = new PendingRegistration(
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

        ConfirmRegistrationResponse response = service.confirmarCodigo(email, code);

        assertTrue(response.sucesso());
        assertEquals("Cadastro confirmado com sucesso.", response.mensagem());
        assertNotNull(response.usuario());
        assertEquals(email, response.usuario().getEmail());

        verify(userRepository).saveUser(any(User.class));
        verify(pendingStore).delete(email);
        verifyNoInteractions(codeSender); // confirmar não envia de novo
    }
}

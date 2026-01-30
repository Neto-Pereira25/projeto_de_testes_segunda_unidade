/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package HelenaTest;

import org.junit.jupiter.api.Test ;
    import org.junit.jupiter.api.extension.ExtendWith ;

    import org.mockito.ArgumentCaptor ;
    import org.mockito.InjectMocks ;
    import org.mockito.Mock ;
    import org.mockito.junit.jupiter.MockitoExtension ;

    import java.time.LocalDate ;
    import java.util.UUID ;

    import static org.junit.jupiter.api.Assertions.
    *;
    import static org.mockito.ArgumentMatchers.any ;
    import static org.mockito.Mockito.

    *;

/**
 *
 * @author helena
 */
@ExtendWith(MockitoExtension.class)
public class CadastroUserTest {

        @Mock
        private UsuarioRepository usuarioRepository;

        @Mock
        private PasswordHasher passwordHasher;

        @Mock
        private CpfValidator cpfValidator;

        @InjectMocks
        private CadastroUsuarioService cadastroUsuarioService;

        @Test
        void DeveCadastrarUsuarioDadosValidos() {
            var email = "user@gmail.com";
            var senha = "Senh@123";
            var confirmacaoSenha = "Senh@123"; 
            var nome = "usuario";
            var sobrenome = "silva";
            var cpf = "12345678910";
            var dataNascimento = LocalDate.of(1970, 1, 1);

            var request = new CadastroUsuarioRequest(
                    email, senha, confirmacaoSenha, nome, sobrenome, cpf, dataNascimento
            );

            when(usuarioRepository.existePorEmail(email)).thenReturn(false);
            when(usuarioRepository.existePorCpf(cpf)).thenReturn(false);
            when(cpfValidator.isValid(cpf)).thenReturn(true);
            when(passwordHasher.hash(senha)).thenReturn("HASHED_123");

            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
                Usuario u = inv.getArgument(0);
                u.setId(UUID.randomUUID());
                return u;
            });

            CadastroUsuarioResponse response = cadastroUsuarioService.cadastrar(request);

            assertTrue(response.sucesso());
            assertEquals("Cadastro realizado com sucesso.", response.mensagem());
            assertNotNull(response.usuarioId());

            ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
            verify(usuarioRepository).save(captor.capture());

            Usuario salvo = captor.getValue();
            assertEquals(email, salvo.getEmail());
            assertEquals(nome, salvo.getNome());
            assertEquals(sobrenome, salvo.getSobrenome());
            assertEquals("HASHED_123", salvo.getSenhaHash());
            assertEquals(cpf, salvo.getCpf());
            assertEquals(dataNascimento, salvo.getDataNascimento());

            // Verifica o fluxo
            verify(usuarioRepository).existePorEmail(email);
            verify(usuarioRepository).existePorCpf(cpf);
            verify(cpfValidator).isValid(cpf);
            verify(passwordHasher).hash(senha);
        }
}

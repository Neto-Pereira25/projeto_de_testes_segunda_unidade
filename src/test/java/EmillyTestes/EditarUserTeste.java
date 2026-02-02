package EmillyTestes;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.EditUserService;
import com.teste.dsc.projetodetestessegundaunidade.utils.EmailValidator;
import com.teste.dsc.projetodetestessegundaunidade.utils.PasswordValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EditarUserTeste {

    @Mock
    private UserRepository userRepository;

    private EditUserService editUserService;

    @BeforeEach
    void setup() {
        // Validators reais (Mockito não consegue mockar EmailValidator no seu projeto)
        editUserService = new EditUserService(userRepository, new EmailValidator(), new PasswordValidator());
    }

    @Test
    void TC_035_atualizarPerfil_usuarioAutenticado_comCamposPreenchidos() {
        // Dados do TC_035
        String email = "user@gmail.com";
        String senhaAtual = "SenhaAtual@123";

        // Na planilha aparece ******** (mascarado), aqui usamos uma senha válida pro validator
        String novaSenha = "NovaSenha@123";

        String endereco = "Rua dos bobos";
        int numero = 0;
        String cep = "00000-000";
        String complemento = "Nao tem teto, chao nem parede";
        String pontoRef = "Nao possui";

        User userLogado = new User(email, senhaAtual, senhaAtual, "Nome", "Sobrenome", "12345678900", "2000-01-01");
        when(userRepository.findByEmailAndPassword(email, senhaAtual)).thenReturn(userLogado);

        // Act
        editUserService.updateProfile(
                email, senhaAtual,
                email, novaSenha,
                endereco, numero, cep, complemento, pontoRef
        );

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveUser(captor.capture());

        User salvo = captor.getValue();
        assertEquals(email, salvo.getEmail());
        assertEquals(novaSenha, salvo.getPassword());
        assertEquals(novaSenha, salvo.getPasswordConfirmation());

        assertEquals(endereco, salvo.getAddress());
        assertEquals(numero, salvo.getNumber());
        assertEquals(cep, salvo.getCep());
        assertEquals(complemento, salvo.getComplement());
        assertEquals(pontoRef, salvo.getReferencePoint());
    }

    @Test
    void TC_036_atualizarPerfil_emailInvalido_deveFalharENaoSalvar() {
        // Dados do TC_036
        String emailAtual = "user@gmail.com";
        String senhaAtual = "SenhaAtual@123";

        // Email inválido (sem @) conforme cenário
        String emailInvalido = "usergmail.com";

        // Senha válida pro validator
        String novaSenha = "NovaSenha@123";

        String endereco = "Rua dos bobos";
        int numero = 0;
        String cep = "00000-000";
        String complemento = "Nao tem teto, chao nem parede";
        String pontoRef = "Nao possui";

        User userLogado = new User(emailAtual, senhaAtual, senhaAtual, "Nome", "Sobrenome", "12345678900", "2000-01-01");
        when(userRepository.findByEmailAndPassword(emailAtual, senhaAtual)).thenReturn(userLogado);

        // Act + Assert (deve lançar exceção)
        assertThrows(BusinessRuleException.class, () ->
                editUserService.updateProfile(
                        emailAtual, senhaAtual,
                        emailInvalido, novaSenha,
                        endereco, numero, cep, complemento, pontoRef
                )
        );

        // Não pode salvar
        verify(userRepository, never()).saveUser(any(User.class));
    }
}

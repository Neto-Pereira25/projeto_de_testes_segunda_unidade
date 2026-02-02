package EmillyTestes;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EditarUserTeste {

    @Mock private UserRepository userRepository;

    private EditUserService editUserService;

    @BeforeEach
    void setup() {
        editUserService = new EditUserService(userRepository, new EmailValidator(), new PasswordValidator());
    }

    @Test
    void TC_035_atualizarPerfil_usuarioAutenticado_comCamposPreenchidos() {
        String email = "user@gmail.com";
        String senhaAtual = "SenhaAtual@123";

       
        String novaSenha = "NovaSenha@123";

        String endereco = "Rua dos bobos";
        int numero = 0;
        String cep = "00000-000";
        String complemento = "Nao tem teto, chao nem parede";
        String pontoRef = "Nao possui";

        User userLogado = new User(email, senhaAtual, senhaAtual, "Nome", "Sobrenome", "12345678900", "2000-01-01");

        when(userRepository.findByEmailAndPassword(email, senhaAtual)).thenReturn(userLogado);

        editUserService.updateProfile(
                email, senhaAtual,
                email, novaSenha,
                endereco, numero, cep, complemento, pontoRef
        );

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
}

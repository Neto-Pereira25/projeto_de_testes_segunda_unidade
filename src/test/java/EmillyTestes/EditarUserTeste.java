package EmillyTestes;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.EditUserService;
import com.teste.dsc.projetodetestessegundaunidade.services.VerificationCodeService;
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

/**
 *
 * @author Emilly Maria
 */
@ExtendWith(MockitoExtension.class)
public class EditarUserTeste {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationCodeService verificationCodeService;

    private EditUserService editUserService;

    @BeforeEach
    void setup() {
        
        editUserService = new EditUserService(
                userRepository,
                new EmailValidator(),
                new PasswordValidator(),
                verificationCodeService
        );
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

    @Test
    void TC_036_atualizarPerfil_emailInvalido_deveFalharENaoSalvar() {
        String emailAtual = "user@gmail.com";
        String senhaAtual = "SenhaAtual@123";

        String emailInvalido = "usergmail.com";
        String novaSenha = "NovaSenha@123";

        String endereco = "Rua dos bobos";
        int numero = 0;
        String cep = "00000-000";
        String complemento = "Nao tem teto, chao nem parede";
        String pontoRef = "Nao possui";

        User userLogado = new User(emailAtual, senhaAtual, senhaAtual, "Nome", "Sobrenome", "12345678900", "2000-01-01");
        when(userRepository.findByEmailAndPassword(emailAtual, senhaAtual)).thenReturn(userLogado);

        assertThrows(BusinessRuleException.class, () ->
                editUserService.updateProfile(
                        emailAtual, senhaAtual,
                        emailInvalido, novaSenha,
                        endereco, numero, cep, complemento, pontoRef
                )
        );

        verify(userRepository, never()).saveUser(any(User.class));
    }

    @Test
    void TC_037_atualizarEmail_comCodigo_valido_deveAtualizarESalvar() {
        // Dados do TC_037
        String emailAtual = "user@gmail.com";
        String senhaAtual = "SenhaAtual@123";

        String novoEmail = "useratualizado@gmail.com"; 
        String codigoDigitado = "123456";             
        String novaSenha = "NovaSenha@123";            

        String endereco = "Rua dos bobos";
        int numero = 0;
        String cep = "00000-000";
        String complemento = "Nao tem teto, chao nem parede";
        String pontoRef = "Nao possui";

        User userLogado = new User(emailAtual, senhaAtual, senhaAtual, "Nome", "Sobrenome", "12345678900", "2000-01-01");

        when(userRepository.findByEmailAndPassword(emailAtual, senhaAtual)).thenReturn(userLogado);
        when(verificationCodeService.sendCode(novoEmail)).thenReturn(codigoDigitado);
        when(verificationCodeService.isValid(novoEmail, codigoDigitado)).thenReturn(true);

        editUserService.updateEmailWithCode(
                emailAtual, senhaAtual,
                novoEmail, codigoDigitado,
                novaSenha,
                endereco, numero, cep, complemento, pontoRef
        );

        verify(verificationCodeService).sendCode(novoEmail);
        verify(verificationCodeService).isValid(novoEmail, codigoDigitado);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveUser(captor.capture());

        User salvo = captor.getValue();
        assertEquals(novoEmail, salvo.getEmail());
    }
    
    @Test
    void TC_038_atualizarEmail_comCodigo_incorreto_deveFalharENaoSalvar() {
        String emailAtual = "user@gmail.com";
        String senhaAtual = "SenhaAtual@123";

        String novoEmail = "userdeoutrapessoa@gmail.com"; 
        String codigoCorreto = "123456";                  
        String codigoIncorreto = "000000";                
        String novaSenha = "NovaSenha@123";              

        String endereco = "Rua dos bobos";
        int numero = 0;
        String cep = "00000-000";
        String complemento = "Nao tem teto, chao nem parede";
        String pontoRef = "Nao possui";

        User userLogado = new User(emailAtual, senhaAtual, senhaAtual, "Nome", "Sobrenome", "12345678900", "2000-01-01");

        when(userRepository.findByEmailAndPassword(emailAtual, senhaAtual)).thenReturn(userLogado);
        when(verificationCodeService.sendCode(novoEmail)).thenReturn(codigoCorreto);
        when(verificationCodeService.isValid(novoEmail, codigoIncorreto)).thenReturn(false);

        assertThrows(BusinessRuleException.class, ()
                -> editUserService.updateEmailWithCode(
                        emailAtual, senhaAtual,
                        novoEmail, codigoIncorreto,
                        novaSenha,
                        endereco, numero, cep, complemento, pontoRef
                )
        );

        verify(verificationCodeService).sendCode(novoEmail);
        verify(verificationCodeService).isValid(novoEmail, codigoIncorreto);

        verify(userRepository, never()).saveUser(any(User.class));
    }

}

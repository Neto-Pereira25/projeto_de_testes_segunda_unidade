package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.utils.EmailValidator;
import com.teste.dsc.projetodetestessegundaunidade.utils.PasswordValidator;

/**
 *
 * @author Emilly Maria
 */
public class EditUserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    // para TC_037/TC_038
    private final VerificationCodeService verificationCodeService;

    public EditUserService(UserRepository userRepository, EmailValidator emailValidator, PasswordValidator passwordValidator) {
        this(userRepository, emailValidator, passwordValidator, null);
    }

    public EditUserService(UserRepository userRepository,
                           EmailValidator emailValidator,
                           PasswordValidator passwordValidator,
                           VerificationCodeService verificationCodeService) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.verificationCodeService = verificationCodeService;
    }

    // TC_035 / TC_036 / TC_039 / TC_040 / TC_041 / TC_042
    public User updateProfile(
            String emailAtual,
            String senhaAtual,
            String novoEmail,
            String novaSenha,
            String endereco,
            int numero,
            String cep,
            String complemento,
            String pontoDeReferencia
    ) {
        // 1) autentica primeiro
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        // 2) valida campos obrigatórios (vazios)
        if (novoEmail == null || novoEmail.trim().isEmpty())
            throw new BusinessRuleException("Campo email obrigatorio");

        if (endereco == null || endereco.trim().isEmpty())
            throw new BusinessRuleException("Campo endereco obrigatorio");

        if (numero < 0)
            throw new BusinessRuleException("Campo numero obrigatorio");

        if (cep == null || cep.trim().isEmpty())
            throw new BusinessRuleException("Campo cep obrigatorio");

        // 3) valida regras de formato
        if (!emailValidator.isValid(novoEmail))
            throw new BusinessRuleException("Email invalido");

        if (!passwordValidator.isValid(novaSenha))
            throw new BusinessRuleException("Senha invalida");

        // 4) atualiza e salva
        user.setEmail(novoEmail);
        user.setPassword(novaSenha);
        user.setPasswordConfirmation(novaSenha);

        user.setAddress(endereco);
        user.setNumber(numero);
        user.setCep(cep);
        user.setComplement(complemento);
        user.setReferencePoint(pontoDeReferencia);

        return userRepository.saveUser(user);
    }

    // TC_037 / TC_038 (com código)
    public User updateEmailWithCode(
            String emailAtual,
            String senhaAtual,
            String novoEmail,
            String codigoDigitado,
            String novaSenha,
            String endereco,
            int numero,
            String cep,
            String complemento,
            String pontoDeReferencia
    ) {
        // 1) autentica primeiro
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        // 2) valida campos obrigatórios (mantém coerência com updateProfile)
        if (novoEmail == null || novoEmail.trim().isEmpty())
            throw new BusinessRuleException("Campo email obrigatorio");

        if (endereco == null || endereco.trim().isEmpty())
            throw new BusinessRuleException("Campo endereco obrigatorio");

        if (numero < 0)
            throw new BusinessRuleException("Campo numero obrigatorio");

        if (cep == null || cep.trim().isEmpty())
            throw new BusinessRuleException("Campo cep obrigatorio");

        // 3) valida regras de formato
        if (!emailValidator.isValid(novoEmail))
            throw new BusinessRuleException("Email invalido");

        if (!passwordValidator.isValid(novaSenha))
            throw new BusinessRuleException("Senha invalida");

        // 4) fluxo do código
        if (verificationCodeService == null)
            throw new BusinessRuleException("Servico de codigo indisponivel");

        verificationCodeService.sendCode(novoEmail);

        if (!verificationCodeService.isValid(novoEmail, codigoDigitado)) {
            throw new BusinessRuleException("Codigo invalido");
        }

        // 5) atualiza e salva
        user.setEmail(novoEmail);
        user.setPassword(novaSenha);
        user.setPasswordConfirmation(novaSenha);

        user.setAddress(endereco);
        user.setNumber(numero);
        user.setCep(cep);
        user.setComplement(complemento);
        user.setReferencePoint(pontoDeReferencia);

        return userRepository.saveUser(user);
    }
}

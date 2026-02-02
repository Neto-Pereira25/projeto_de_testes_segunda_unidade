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

    // para TC_037
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

    // TC_035 / TC_036
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
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        if (!emailValidator.isValid(novoEmail)) throw new BusinessRuleException("Email invalido");
        if (!passwordValidator.isValid(novaSenha)) throw new BusinessRuleException("Senha invalida");

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

    // TC_037: atualização do e-mail com confirmação por código
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
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        if (!emailValidator.isValid(novoEmail)) throw new BusinessRuleException("Email invalido");
        if (!passwordValidator.isValid(novaSenha)) throw new BusinessRuleException("Senha invalida");

        if (verificationCodeService == null) throw new BusinessRuleException("Servico de codigo indisponivel");

        // Fluxo do cenário: envia código e valida o código digitado
        verificationCodeService.sendCode(novoEmail);

        if (!verificationCodeService.isValid(novoEmail, codigoDigitado)) {
            throw new BusinessRuleException("Codigo invalido");
        }

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

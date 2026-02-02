package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.utils.EmailValidator;
import com.teste.dsc.projetodetestessegundaunidade.utils.PasswordValidator;

public class EditUserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public EditUserService(UserRepository userRepository, EmailValidator emailValidator, PasswordValidator passwordValidator) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
    }

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
        // usuario "logado" (simulado)
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        // validacoes
        if (!emailValidator.isValid(novoEmail)) throw new BusinessRuleException("Email invalido");
        if (!passwordValidator.isValid(novaSenha)) throw new BusinessRuleException("Senha invalida");

        // atualiza campos do TC_035
        user.setEmail(novoEmail);
        user.setPassword(novaSenha);
        user.setPasswordConfirmation(novaSenha); // mantém coerente com o modelo atual

        user.setAddress(endereco);
        user.setNumber(numero);
        user.setCep(cep);
        user.setComplement(complemento);
        user.setReferencePoint(pontoDeReferencia);

        return userRepository.saveUser(user);
    }
}

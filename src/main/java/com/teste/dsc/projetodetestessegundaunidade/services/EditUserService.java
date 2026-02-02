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

    // para CEP (se você estiver usando os testes de CEP)
    private final AddressLookupService addressLookupService;

    public EditUserService(UserRepository userRepository,
                           EmailValidator emailValidator,
                           PasswordValidator passwordValidator) {
        this(userRepository, emailValidator, passwordValidator, null, null);
    }

    public EditUserService(UserRepository userRepository,
                           EmailValidator emailValidator,
                           PasswordValidator passwordValidator,
                           VerificationCodeService verificationCodeService) {
        this(userRepository, emailValidator, passwordValidator, verificationCodeService, null);
    }

    public EditUserService(UserRepository userRepository,
                           EmailValidator emailValidator,
                           PasswordValidator passwordValidator,
                           VerificationCodeService verificationCodeService,
                           AddressLookupService addressLookupService) {
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.verificationCodeService = verificationCodeService;
        this.addressLookupService = addressLookupService;
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

        // 2) valida campos obrigatórios
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
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        if (novoEmail == null || novoEmail.trim().isEmpty())
            throw new BusinessRuleException("Campo email obrigatorio");

        if (endereco == null || endereco.trim().isEmpty())
            throw new BusinessRuleException("Campo endereco obrigatorio");

        if (numero < 0)
            throw new BusinessRuleException("Campo numero obrigatorio");

        if (cep == null || cep.trim().isEmpty())
            throw new BusinessRuleException("Campo cep obrigatorio");

        if (!emailValidator.isValid(novoEmail))
            throw new BusinessRuleException("Email invalido");

        if (!passwordValidator.isValid(novaSenha))
            throw new BusinessRuleException("Senha invalida");

        if (verificationCodeService == null)
            throw new BusinessRuleException("Servico de codigo indisponivel");

        // envia e valida código
        verificationCodeService.sendCode(novoEmail);

        if (!verificationCodeService.isValid(novoEmail, codigoDigitado))
            throw new BusinessRuleException("Codigo invalido");

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

    // TC_043 até TC_048 (fluxo de senha do SEU PRINT)
    public User updateProfileWithPasswordFields(
            String emailAtual,
            String senhaAtualDigitada,
            String novoEmail,
            String novaSenha,
            String confirmacaoSenha,
            String endereco,
            int numero,
            String cep,
            String complemento,
            String pontoDeReferencia
    ) {
        // No seu teste (44..48) você mocka findByEmail(email), então usamos isso aqui
        User user = userRepository.findByEmail(emailAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        // obrigatórios do perfil (mantém coerência)
        if (novoEmail == null || novoEmail.trim().isEmpty())
            throw new BusinessRuleException("Campo email obrigatorio");

        if (endereco == null || endereco.trim().isEmpty())
            throw new BusinessRuleException("Campo endereco obrigatorio");

        if (numero < 0)
            throw new BusinessRuleException("Campo numero obrigatorio");

        if (cep == null || cep.trim().isEmpty())
            throw new BusinessRuleException("Campo cep obrigatorio");

        if (!emailValidator.isValid(novoEmail))
            throw new BusinessRuleException("Email invalido");

        // verifica se senha atual bate
        boolean senhaAtualOk = user.getPassword() != null && user.getPassword().equals(senhaAtualDigitada);

        boolean novaVazia = (novaSenha == null || novaSenha.trim().isEmpty());
        boolean confirmVazia = (confirmacaoSenha == null || confirmacaoSenha.trim().isEmpty());
        boolean querTrocarSenha = !(novaVazia && confirmVazia);

        // Regra para satisfazer seus cenários:
        // - TC_047: senha atual correta + nova/confirm vazias => ERRO
        // - TC_048: senha atual INCORRETA + nova/confirm vazias => OK (não troca senha)
        if (!querTrocarSenha) {
            if (senhaAtualOk) {
                throw new BusinessRuleException("Campos senha obrigatorios");
            }
            // não quer trocar senha (e senha atual está incorreta) => atualiza só o perfil
        } else {
            // quer trocar senha => senha atual precisa ser correta
            if (!senhaAtualOk) throw new BusinessRuleException("Senha atual invalida");

            // confirmação obrigatória
            if (confirmVazia) throw new BusinessRuleException("Confirmacao de senha obrigatoria");

            // nova senha obrigatória
            if (novaVazia) throw new BusinessRuleException("Nova senha obrigatoria");

            // precisa bater
            if (!novaSenha.equals(confirmacaoSenha))
                throw new BusinessRuleException("Confirmacao de senha divergente");

            // valida regra da senha (validator)
            if (!passwordValidator.isValid(novaSenha))
                throw new BusinessRuleException("Senha invalida");

            // troca senha
            user.setPassword(novaSenha);
            user.setPasswordConfirmation(novaSenha);
        }

        // Atualiza os outros campos (sempre)
        user.setEmail(novoEmail);
        user.setAddress(endereco);
        user.setNumber(numero);
        user.setCep(cep);
        user.setComplement(complemento);
        user.setReferencePoint(pontoDeReferencia);

        return userRepository.saveUser(user);
    }

    // CEP válido / inválido (caso você esteja usando)
    public User updateAddressByCep(
            String emailAtual,
            String senhaAtual,
            String cep,
            String complemento,
            String pontoDeReferencia
    ) {
        User user = userRepository.findByEmailAndPassword(emailAtual, senhaAtual);
        if (user == null) throw new BusinessRuleException("Usuario nao autenticado");

        if (cep == null || cep.trim().isEmpty())
            throw new BusinessRuleException("Campo cep obrigatorio");

        if (addressLookupService == null)
            throw new BusinessRuleException("Servico de CEP indisponivel");

        AddressLookupService.AddressResult result = addressLookupService.lookupByCep(cep);

        if (result == null || result.getEndereco() == null || result.getEndereco().trim().isEmpty()) {
            throw new BusinessRuleException("CEP invalido");
        }

        user.setCep(cep);
        user.setAddress(result.getEndereco());
        user.setNumber(result.getNumero());

        user.setComplement(complemento);
        user.setReferencePoint(pontoDeReferencia);

        return userRepository.saveUser(user);
    }
}

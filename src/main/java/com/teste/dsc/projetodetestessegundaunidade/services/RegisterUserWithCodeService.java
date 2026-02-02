/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.utils.EmailValidator;
import com.teste.dsc.projetodetestessegundaunidade.utils.PasswordValidator;

/**
 *
 * @author helena
 */
public class RegisterUserWithCodeService {

    public interface VerificationCodeSender {

        void send(String email, String code);
    }

    public interface VerificationCodeGenerator {

        String generateCode();
    }

    public interface PendingRegistrationStore {

        void save(String email, PendingRegistration pending);

        PendingRegistration findByEmail(String email);

        void delete(String email);
    }

    public static class PendingRegistration {

        private final String email;
        private final String password;
        private final String passwordConfirmation;
        private final String name;
        private final String surname;
        private final String cpf;
        private final String birthDate;
        private final String code;

        public PendingRegistration(String email, String password, String passwordConfirmation,
                String name, String surname, String cpf, String birthDate, String code) {
            this.email = email;
            this.password = password;
            this.passwordConfirmation = passwordConfirmation;
            this.name = name;
            this.surname = surname;
            this.cpf = cpf;
            this.birthDate = birthDate;
            this.code = code;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getPasswordConfirmation() {
            return passwordConfirmation;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getCpf() {
            return cpf;
        }

        public String getBirthDate() {
            return birthDate;
        }

        public String getCode() {
            return code;
        }
    }

    private final UserRepository userRepository;
    private final PendingRegistrationStore pendingStore;
    private final VerificationCodeSender codeSender;
    private final VerificationCodeGenerator codeGenerator;

    public RegisterUserWithCodeService(UserRepository userRepository,
            PendingRegistrationStore pendingStore,
            VerificationCodeSender codeSender,
            VerificationCodeGenerator codeGenerator) {
        this.userRepository = userRepository;
        this.pendingStore = pendingStore;
        this.codeSender = codeSender;
        this.codeGenerator = codeGenerator;
    }

    // envia o código e aguarda
    public String solicitarCodigo(String email,
            String password,
            String passwordConfirmation,
            String name,
            String surname,
            String cpf,
            String birthDate) {

        // Validações pra chegar no envio do código
        if (email == null || email.isBlank()) {
            throw new BusinessRuleException("Campo e-mail precisa ser preenchido.");
        }

        if (name == null || name.isBlank()
                || surname == null || surname.isBlank()
                || cpf == null || cpf.isBlank()
                || password == null || password.isBlank()
                || passwordConfirmation == null || passwordConfirmation.isBlank()
                || birthDate == null || birthDate.isBlank()) {
            throw new BusinessRuleException("Invalid required fields");
        }

        if (!EmailValidator.isValid(email)) {
            throw new BusinessRuleException("Campo e-mail é inválido.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("O e-mail informado já possui cadastro.");
        }

        if (!PasswordValidator.isValid(password)) {
            throw new BusinessRuleException("Password field is invalid.");
        }
        if (!PasswordValidator.isValid(passwordConfirmation)) {
            throw new BusinessRuleException("Password confirmation field is invalid.");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new BusinessRuleException("Password and password confirmation must be the same!");
        }

        String code = codeGenerator.generateCode();
        PendingRegistration pending = new PendingRegistration(
                email, password, passwordConfirmation, name, surname, cpf, birthDate, code
        );

        pendingStore.save(email, pending);
        codeSender.send(email, code);

        return "Código enviado para o usuário. Aguarde a confirmação.";
    }

    //  confirma o código e conclui o cadastro
    public String confirmarCodigo(String email, String code) {
        if (email == null || email.isBlank()) {
            throw new BusinessRuleException("Campo e-mail precisa ser preenchido.");
        }
        if (code == null || code.isBlank()) {
            throw new BusinessRuleException("Código precisa ser preenchido.");
        }

        PendingRegistration pending = pendingStore.findByEmail(email);
        if (pending == null) {
            throw new BusinessRuleException("Nenhum cadastro pendente para este e-mail.");
        }

        if (!pending.getCode().equals(code)) {
            throw new BusinessRuleException("Código inválido.");
        }

        User user = new User(
                pending.getEmail(),
                pending.getPassword(),
                pending.getPasswordConfirmation(),
                pending.getName(),
                pending.getSurname(),
                pending.getCpf(),
                pending.getBirthDate()
        );

        userRepository.saveUser(user);
        pendingStore.delete(email);

        return "Cadastro confirmado com sucesso.";
    }
}

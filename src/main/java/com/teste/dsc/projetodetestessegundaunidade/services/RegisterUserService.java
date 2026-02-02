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
 * @author Neto Pereira
 */
public class RegisterUserService {

    private final UserRepository userRepository;

    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String email,
            String password,
            String passwordConfirmation,
            String name,
            String surname,
            String cpf,
            String birthDate) {

        validateRequiredFields(email, password, passwordConfirmation, name, surname, cpf, birthDate);

        validateName(name);
        validateSurname(surname);
        validateCpf(cpf);

        if (!EmailValidator.isValid(email)) {
            throw new BusinessRuleException("Email field is invalid.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("O e-mail informado já possui cadastro.");
        }

        if (password == null || password.isBlank() || !PasswordValidator.isValid(password)) {
            throw new BusinessRuleException("Password field is invalid.");
        }

        if (passwordConfirmation == null || passwordConfirmation.isBlank()
                || !PasswordValidator.isValid(passwordConfirmation)) {
            throw new BusinessRuleException("Password confirmation field is invalid.");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new BusinessRuleException("Password and password confirmation must be the same!");
        }

        User user = new User(email, password, passwordConfirmation, name, surname, cpf, birthDate);

        User userRegistered = userRepository.saveUser(user);

        return userRegistered;
    }

    private void validateRequiredFields(String email,
            String password,
            String passwordConfirmation,
            String name,
            String surname,
            String cpf,
            String birthDate) {
        if (email == null || email.isBlank()) {
            throw new BusinessRuleException("Campo e-mail precisa ser preenchido.");
        }

        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Campo nome está vazio.");
        }

        if (name == null || name.isBlank()
                || surname == null || surname.isBlank()
                || email == null || email.isBlank()
                || cpf == null || cpf.isBlank()
                || birthDate == null || birthDate.isBlank()) {

            throw new BusinessRuleException("Invalid required fields");
        }
    }

    private void validateName(String name) {
        String regex = "^[\\p{L} ]+$";
        if (!name.matches(regex)) {
            throw new BusinessRuleException("Campo nome possui caracteres inválidos.");
        }
    }

    private void validateSurname(String surname) {
        String regex = "^[\\p{L} ]+$";
        if (!surname.matches(regex)) {
            throw new BusinessRuleException("Campo sobrenome possui caracteres inválidos.");
        }
    }

    private void validateCpf(String cpf) {
        if (!cpf.matches("^\\d{11}$")) {
            throw new BusinessRuleException("Campo cpf inválido.");
        }

        if (cpf.chars().distinct().count() == 1) {
            throw new BusinessRuleException("Campo cpf inválido.");
        }
    }

    private void validateEmail(String email) {
        // exige algo@algo.algo (sem espaços)
        String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$";
        if (!email.matches(regex)) {
            throw new BusinessRuleException("Invalid email");
        }
    }

}

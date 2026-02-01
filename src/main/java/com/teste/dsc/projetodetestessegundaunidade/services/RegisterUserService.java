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
        
        if (!PasswordValidator.isValid(password)) {
            throw new BusinessRuleException("Password field is invalid.");
        }
        
        if (!PasswordValidator.isValid(passwordConfirmation)) {
            throw new BusinessRuleException("Password confirmation field is invalid.");
        }
        
        if (!EmailValidator.isValid(email)) {
            throw new BusinessRuleException("Email field is invalid.");
        }

        if (!password.equals(passwordConfirmation)) {
            throw new BusinessRuleException("Password and password confirmation must be the same!");
        }

        validateRequiredFields(email, password, passwordConfirmation, name, surname, cpf, birthDate);

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
        if (name == null || name.isBlank()
                || surname == null || surname.isBlank()
                || email == null || email.isBlank()
                || cpf == null || cpf.isBlank()
                || password == null || password.isBlank()
                || passwordConfirmation == null || passwordConfirmation.isBlank()
                || birthDate == null || birthDate.isBlank()) {

            throw new BusinessRuleException("Invalid required fields");
        }
    }

}

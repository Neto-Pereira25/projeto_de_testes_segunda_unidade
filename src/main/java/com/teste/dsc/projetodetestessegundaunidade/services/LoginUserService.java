package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;
import com.teste.dsc.projetodetestessegundaunidade.utils.EmailValidator;
import com.teste.dsc.projetodetestessegundaunidade.utils.PasswordValidator;

public class LoginUserService {

    private final UserRepository userRepository;

    public LoginUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessRuleException("Email cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BusinessRuleException("Password cannot be empty");
        }

        if (!PasswordValidator.isValid(password)) {
            throw new BusinessRuleException("Password field is invalid.");
        }

        if (!EmailValidator.isValid(email)) {
            throw new BusinessRuleException("Email field is invalid.");
        }

        User userFound = userRepository.findByEmail(email);

        if (userFound == null) {
            throw new BusinessRuleException("This email address is not registered.");
        }

        User user = userRepository.findByEmailAndPassword(email, password);

        if (user == null) {
            throw new BusinessRuleException("Invalid email or password");
        }

        return user;
    }
}

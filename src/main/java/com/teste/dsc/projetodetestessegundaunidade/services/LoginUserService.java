package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;

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
        
        User userFound = userRepository.findByEmail(email);
        
        if (userFound == null){
            throw new BusinessRuleException("This email address is not registered.");
        }
        
        User user = userRepository.findByEmailAndPassword(email, password);

        if (user == null) {
            throw new BusinessRuleException("Invalid email or password");
        }

        return user;
    }
}

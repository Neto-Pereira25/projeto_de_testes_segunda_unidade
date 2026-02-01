package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;

public class RecoverPasswordService {
    private final UserRepository userRepository;

    public RecoverPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User recover(String email, String newPassword, String confirmNewPassword) {
        
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessRuleException("Email cannot be empty");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessRuleException("New password cannot be empty");
        }

        if (confirmNewPassword == null || confirmNewPassword.trim().isEmpty()) {
            throw new BusinessRuleException("Confirm new password cannot be empty");
        }
        
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            throw new BusinessRuleException("This email address is not registered.");
        }
        
        user.setPassword(newPassword);
        user.setPasswordConfirmation(confirmNewPassword);
        
        return user;
    }
}

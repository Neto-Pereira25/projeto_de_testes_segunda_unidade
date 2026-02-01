package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;

public class RecoverPasswordService {
    private final UserRepository userRepository;

    public RecoverPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User recover(String email, String newPassword, String confirmNewPassword) {
        User user = userRepository.findByEmail(email);
        
        user.setPassword(newPassword);
        user.setPasswordConfirmation(confirmNewPassword);
        
        return user;
    }
}

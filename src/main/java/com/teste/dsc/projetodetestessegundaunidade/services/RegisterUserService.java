/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.UserRepository;

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
        
        if (!password.equals(passwordConfirmation)) {
            throw new BusinessRuleException("Password and password confirmation must be the same!");
        }
        
        User user = new User(email, password, passwordConfirmation, name, surname, cpf, birthDate);
        
        User userRegistered = userRepository.saveUser(user);
        
        return userRegistered;
    }

}

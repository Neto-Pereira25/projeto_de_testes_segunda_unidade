package com.teste.dsc.projetodetestessegundaunidade.repositories;

import com.teste.dsc.projetodetestessegundaunidade.entities.User;

public interface UserRepository {
    User saveUser(User user);
    
    User findByEmailAndPassword(String email, String password);
}

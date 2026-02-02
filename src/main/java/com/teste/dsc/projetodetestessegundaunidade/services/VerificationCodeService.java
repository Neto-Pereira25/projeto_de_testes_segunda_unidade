package com.teste.dsc.projetodetestessegundaunidade.services;
/**
 *
 * @author Emilly Maria
 */
public interface VerificationCodeService {
    String sendCode(String email);                 
    boolean isValid(String email, String code);    
}

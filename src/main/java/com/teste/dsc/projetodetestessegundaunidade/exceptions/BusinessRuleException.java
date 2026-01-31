/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teste.dsc.projetodetestessegundaunidade.exceptions;

/**
 *
 * @author Neto Pereira
 */
public class BusinessRuleException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public BusinessRuleException(String msg) {
        super(msg);
    }
    
    
}

package com.teste.dsc.projetodetestessegundaunidade.utils;

public class PasswordValidator {
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])" +        // letra minúscula
            "(?=.*[A-Z])" +         // letra maiúscula
            "(?=.*\\d)" +           // número
            "(?=.*[^A-Za-z0-9])" +  // caractere especial
            ".{8,32}$";              // tamanho

    public static boolean isValid(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return password.matches(PASSWORD_REGEX);
    }
}

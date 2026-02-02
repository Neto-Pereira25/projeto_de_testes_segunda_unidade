/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teste.dsc.projetodetestessegundaunidade.entities;

/**
 *
 * @author akind
 */
public class Pet {

    private String nome;
    private String raca;
    private String idade;
    private String foto;
    private String hashChip;

    // Construtor padrão
    public Pet() {
    }

    public Pet(String nome, String raca, String idade, String foto, String hashChip) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.foto = foto;
        this.hashChip = hashChip;
    }

    
    public String getNome() {
        return nome;
    }

    public String getHashChip() {
        return hashChip;
    }
 
}

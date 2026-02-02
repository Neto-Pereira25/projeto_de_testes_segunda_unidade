package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.Pet;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.PetRepository;

public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet cadastrarPet(String nome, String raca, String idade, String foto, String hashChip) {

        // 1. Validação do Chip (Obrigatório e Formato)
        if (hashChip == null || hashChip.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo Informações do chip: preenchimento obrigatório.");
        }
        if (!hashChip.matches("[a-zA-Z0-9-]+")) {
            throw new BusinessRuleException("Erro no campo Informações do chip: caracteres especiais não são permitidos.");
        }

        // 2. Validação do Nome (Obrigatório e Formato)
        // CORREÇÃO: Primeiro verificamos se é nulo ou vazio
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo nome: preenchimento obrigatório.");
        }
        // CORREÇÃO: Regex único para caracteres e números
        if (!nome.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new BusinessRuleException("Erro no campo nome: caracteres inválidos ou números não são permitidos.");
        }

        // 3. Validação da Raça (Obrigatório e Formato)
        if (raca == null || raca.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo Raça: preenchimento obrigatório.");
        }
        if (!raca.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new BusinessRuleException("Erro no campo Raça: caracteres inválidos.");
        }

        // 4. Validação da Idade (Obrigatório e Formato)
        if (idade == null || idade.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo idade: preenchimento obrigatório.");
        }
        if (!idade.matches("[a-zA-ZÀ-ÿ\\s.]+")) {
            throw new BusinessRuleException("Erro no campo idade: formato inválido ou caracteres não permitidos.");
        }

        // 5. Validação da Foto (Obrigatório e Extensão)
        if (foto == null || foto.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo Foto: preenchimento obrigatório.");
        }
        String fotoLower = foto.toLowerCase();
        if (fotoLower.endsWith(".pdf") || fotoLower.endsWith(".gif")) {
            throw new BusinessRuleException("Erro no campo foto: formato de arquivo não suportado.");
        }

        // 6. Salvamento
        Pet novoPet = new Pet(nome, raca, idade, foto, hashChip);
        return petRepository.save(novoPet);
    }
}
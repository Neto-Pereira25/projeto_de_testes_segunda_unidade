package com.teste.dsc.projetodetestessegundaunidade.services;

import com.teste.dsc.projetodetestessegundaunidade.entities.Pet;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.PetRepository;

/**
 *
 * @author akind
 */
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet cadastrarPet(String nome, String raca, String idade, String foto, String hashChip) {

        // Validação da pré-condição: HashCode obrigatório
        if (hashChip == null || hashChip.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo Informações do chip: preenchimento obrigatório.");
        }

        if (!hashChip.matches("[a-zA-Z0-9-]+")) {
            throw new BusinessRuleException("Erro no campo Informações do chip: caracteres especiais não são permitidos.");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo nome: preenchimento obrigatório.");
        }
        // Validação TC_052 e TC_054: Nome deve conter apenas letras e espaços
        if (!nome.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new BusinessRuleException("Erro no campo nome: caracteres inválidos ou números não são permitidos.");
        }

        if (raca == null || raca.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo Raça: preenchimento obrigatório.");
        }

        if (!raca.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            throw new BusinessRuleException("Erro no campo Raça: caracteres inválidos.");
        }

        if (idade == null || idade.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo idade: preenchimento obrigatório.");
        }

        if (!idade.matches("[a-zA-ZÀ-ÿ\\s.]+")) {
            throw new BusinessRuleException("Erro no campo idade: formato inválido ou caracteres não permitidos.");
        }
        if (foto == null || foto.trim().isEmpty()) {
            throw new BusinessRuleException("Erro no campo Foto: preenchimento obrigatório.");
        }

        String fotoLower = foto.toLowerCase();
        if (fotoLower.endsWith(".pdf") || fotoLower.endsWith(".gif")) {
            throw new BusinessRuleException("Erro no campo foto: formato de arquivo não suportado.");
        }

        Pet novoPet = new Pet(nome, raca, idade, foto, hashChip);
        return petRepository.save(novoPet);
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package akinTest;

/**
 *
 * @author akind
 */
import com.teste.dsc.projetodetestessegundaunidade.entities.Pet;
import com.teste.dsc.projetodetestessegundaunidade.exceptions.BusinessRuleException;
import com.teste.dsc.projetodetestessegundaunidade.repositories.PetRepository;
import com.teste.dsc.projetodetestessegundaunidade.services.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CadastroPet {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    PetService service;

    @Test
    public void testCadastrarPetComSucesso() {

        String nome = "Lua";
        String raca = "Poodle";
        String idade = "Cinco anos.";
        String foto = "ok";
        String hashChip = "HTAS-657";

        Pet petMock = new Pet(nome, raca, idade, foto, hashChip);

        when(petRepository.save(any(Pet.class))).thenReturn(petMock);

        Pet petSalvo = service.cadastrarPet(nome, raca, idade, foto, hashChip);

        assertNotNull(petSalvo);
        assertEquals("Lua", petSalvo.getNome());
        assertEquals("HTAS-657", petSalvo.getHashChip());

        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    public void testCadastroComNomeInvalido() {

        String nomeInvalido = "Lua23";
        String raca = "Poodle";
        String idade = "Cinco anos.";
        String foto = "ok";
        String hashChip = "HTAS-657";

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nomeInvalido, raca, idade, foto, hashChip));

        assertEquals("Erro no campo nome: números não são permitidos.", exception.getMessage());

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastroComCampoRacaInvalido() {

        String nome = "Lua";
        String racaInvalida = "Po0d!e"; // Dado inválido
        String idade = "cinco anos.";
        String foto = "ok";
        String hashChip = "HTAS-657";

       
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, racaInvalida, idade, foto, hashChip));

        
        assertEquals("Erro no campo Raça: caracteres inválidos.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastroComCaractereInvalido() {

        String nomeInvalido = "Lu@"; // Dado com caractere especial
        String raca = "Poodle";
        String idade = "Cinco anos.";
        String foto = "ok";
        String hashChip = "HTAS-657";

      
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nomeInvalido, raca, idade, foto, hashChip));

       
        assertEquals("Erro no campo nome: caracteres inválidos ou números não são permitidos.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testRealizarCadastroComCampoIdadeInvalido() {
        
        String nome = "Lua";
        String raca = "Poodle";
        String idadeInvalida = "5"; // Dado apenas numérico (inválido conforme o cenário)
        String foto = "ok";
        String hashChip = "HTAS-657";

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idadeInvalida, foto, hashChip));

        
        assertEquals("Erro no campo idade: formato inválido.", exception.getMessage());

       
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testRealizarCadastroIdadeCaracteresInvalidos() {
       
        String nome = "Lua";
        String raca = "Poodle";
        String idadeInvalida = "C!nc0 @nos."; // Dado com caracteres especiais e números
        String foto = "ok";
        String hashChip = "HTAS-657";

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idadeInvalida, foto, hashChip));

        
        assertEquals("Erro no campo idade: formato inválido ou caracteres não permitidos.", exception.getMessage());

       
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testRealizarCadastroCampoIdadeEmBranco() {
        
        String nome = "Lua";
        String raca = "Poodle";
        String idadeEmBranco = ""; // Simulando campo não preenchido
        String foto = "ok";
        String hashChip = "HTAS-657";

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idadeEmBranco, foto, hashChip));

        
        assertEquals("Erro no campo idade: preenchimento obrigatório.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testRealizarCadastroNomeEmBranco() {
       
        String nomeEmBranco = ""; // Simulando campo deixado em branco
        String raca = "Poodle";
        String idade = "cinco anos";
        String foto = "ok";
        String hashChip = "HTAS-657";

       
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nomeEmBranco, raca, idade, foto, hashChip));

        
        assertEquals("Erro no campo nome: preenchimento obrigatório.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastrarCampoRacaEmBranco() {
        
        String nome = "Lua";
        String racaEmBranco = ""; // Simulando campo raça deixado em branco
        String idade = "cinco anos";
        String foto = "ok";
        String hashChip = "HTAS-657";

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, racaEmBranco, idade, foto, hashChip));

        
        assertEquals("Erro no campo Raça: preenchimento obrigatório.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastroCampoFotoBranco() {
        
        String nome = "Lua";
        String raca = "Poodle";
        String idade = "cinco anos";
        String fotoEmBranco = ""; // Simulando campo foto não preenchido
        String hashChip = "HTAS-657";

       
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idade, fotoEmBranco, hashChip));

        
        assertEquals("Erro no campo Foto: preenchimento obrigatório.", exception.getMessage());

     
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastroInformacoesChipEmBranco() {
       
        String nome = "Lua";
        String raca = "Poodle";
        String idade = "cinco anos";
        String foto = "ok";
        String hashChipEmBranco = ""; // Simulando campo do chip não preenchido

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idade, foto, hashChipEmBranco));

        
        assertEquals("Erro no campo Informações do chip: preenchimento obrigatório.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastroFotoEmPDF() {
        
        String nome = "Lua";
        String raca = "Poodle";
        String idade = "cinco anos";
        String fotoInvalida = "foto_do_pet.pdf"; // Simulando o envio de um PDF
        String hashChip = "HTAS-657";

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idade, fotoInvalida, hashChip));

        
        assertEquals("O sistema valida os campos e retorna erro no campo foto por ele ser preenchido com um pdf.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCadastroInformacoesChipComCaracteresInvalidos() {
        
        String nome = "Lua";
        String raca = "Poodle";
        String idade = "cinco anos";
        String foto = "foto.png";
        String hashInvalido = "HT?S-6@7"; // Contém '?' e '@'

        // Execução e Verificação
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idade, foto, hashInvalido));

        
        assertEquals("Erro no campo Informações do chip: caracteres especiais não são permitidos.", exception.getMessage());

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void testCampoCadastroPreenchidoComGif() {
        
        String nome = "Lua";
        String raca = "Poodle";
        String idade = "cinco anos";
        String fotoInvalida = "animacao_pet.gif"; // Simulando o envio de um GIF
        String hashChip = "HTAS-657";

        
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> service.cadastrarPet(nome, raca, idade, fotoInvalida, hashChip));

       
        assertEquals("Erro no campo foto: formato de arquivo não suportado.", exception.getMessage());

        
        verify(petRepository, never()).save(any(Pet.class));
    }
}

package com.teste.dsc.projetodetestessegundaunidade.services;

/**
 *
 * @author Emilly Maria
 */
public interface AddressLookupService {

    AddressResult lookupByCep(String cep);

    class AddressResult {
        private final String endereco;
        private final int numero;

        public AddressResult(String endereco, int numero) {
            this.endereco = endereco;
            this.numero = numero;
        }

        public String getEndereco() {
            return endereco;
        }

        public int getNumero() {
            return numero;
        }
    }
}

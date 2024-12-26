package com.crud.gabriel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void criarPessoa() throws Exception {
        String pessoaJson = """
    {
        "nome": "João Silva",
        "cpf": "12345678900",
        "data_nascimento": "1990-05-20",
        "enderecos": [
            {
                "rua": "Rua das Flores",
                "numero": "123",
                "bairro": "Centro",
                "cidade": "Florianópolis",
                "estado": "SC",
                "cep": "88000000",
                "principal": true
            }
        ]
    }
    """;

        mockMvc.perform(post("/pessoas")
                        .contentType("application/json")
                        .content(pessoaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }


    @Test
    public void listarTodasPessoas() throws Exception {
        mockMvc.perform(get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void excluirPessoa() throws Exception {
        mockMvc.perform(delete("/pessoas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void excluirPessoaNaoExistente() throws Exception {
        mockMvc.perform(delete("/pessoas/999"))
                .andExpect(status().isNotFound());
    }
}

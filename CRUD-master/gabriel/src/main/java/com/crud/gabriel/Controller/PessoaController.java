package com.crud.gabriel.Controller;

import com.crud.gabriel.entity.Pessoa;
import com.crud.gabriel.repository.PessoaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
@Tag(name = "Pessoa", description = "Endpoints para gerenciar pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;


    @Operation(summary = "Listar todas as pessoas")
    @GetMapping
    public List<Pessoa> listarTodasPessoas() {
        return pessoaRepository.findAll();
    }


    @Operation(summary = "Criar uma nova pessoa")
    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody @Valid Pessoa pessoa) {
        try {
            Pessoa pessoaSalva = pessoaRepository.save(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Atualizar uma pessoa existente")
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @RequestBody @Valid Pessoa pessoa) {
        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(id);
        if (pessoaExistente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Pessoa pessoaAtualizada = pessoaExistente.get();
        pessoaAtualizada.setNome(pessoa.getNome());
        pessoaAtualizada.setId(pessoa.getId());

        pessoaRepository.save(pessoaAtualizada);
        return new ResponseEntity<>(pessoaAtualizada, HttpStatus.OK);
    }

    @Operation(summary = "Excluir uma pessoa")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id) {
        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(id);
        if (pessoaExistente.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        pessoaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

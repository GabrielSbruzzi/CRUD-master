package com.crud.gabriel.repository;

import com.crud.gabriel.entity.Endereco;
import com.crud.gabriel.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    void delete(Endereco endereco);

    void deleteAllByPessoa(Pessoa pessoaExistente);
}

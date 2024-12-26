package com.crud.gabriel.service;

import com.crud.gabriel.entity.Endereco;
import com.crud.gabriel.entity.Pessoa;
import com.crud.gabriel.repository.EnderecoRepository;
import com.crud.gabriel.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Optional<Pessoa> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    @Autowired
    private EnderecoRepository enderecoRepository;

    public List<Pessoa> listarPessoasComEnderecos() {
        return pessoaRepository.findAll();
    }

    @Transactional
    public Pessoa criarPessoaComEndereco(Pessoa pessoa, List<Endereco> enderecos) {
        validarCamposObrigatorios(pessoa);
        verificarCpfExistente(pessoa.getCpf());

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        for (Endereco endereco : enderecos) {
            endereco.setPessoa(pessoaSalva);
            enderecoRepository.save(endereco);
        }

        return pessoaSalva;
    }

    @Transactional
    public Pessoa atualizarPessoaComEnderecos(Long pessoaId, Pessoa pessoaAtualizada, List<Endereco> novosEnderecos) {
        Pessoa pessoaExistente = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        validarCamposObrigatorios(pessoaAtualizada);

        pessoaExistente.setNome(pessoaAtualizada.getNome());
        pessoaExistente.setDataNascimento(pessoaAtualizada.getDataNascimento());
        pessoaExistente.setCpf(pessoaAtualizada.getCpf());

        pessoaRepository.save(pessoaExistente);

        enderecoRepository.deleteAllByPessoa(pessoaExistente);
        for (Endereco endereco : novosEnderecos) {
            endereco.setPessoa(pessoaExistente);
            enderecoRepository.save(endereco);
        }

        return pessoaExistente;
    }

    @Transactional
    public void excluirPessoa(Long pessoaId) {
        Pessoa pessoaExistente = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        enderecoRepository.deleteAllByPessoa(pessoaExistente);
        pessoaRepository.delete(pessoaExistente);
    }

    public Pessoa obterPessoaPorId(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
    }

    public int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private void validarCamposObrigatorios(Pessoa pessoa) {
        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (pessoa.getCpf() == null || pessoa.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }
    }

    private void verificarCpfExistente(String cpf) {
        if (pessoaRepository.findByCpf(cpf).isPresent()) {
            throw new IllegalArgumentException("CPF já existente.");
        }
    }
}

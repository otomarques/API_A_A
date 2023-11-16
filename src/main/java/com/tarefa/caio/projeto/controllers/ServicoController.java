package com.tarefa.caio.projeto.controllers;

import com.tarefa.caio.projeto.dtos.ServicoDto;
import com.tarefa.caio.projeto.models.ServicoModel;
import com.tarefa.caio.projeto.repositories.ServicoRepository;
import com.tarefa.caio.projeto.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/servicos", produces = {"application/json"})
public class ServicoController {
    @Autowired
    ServicoRepository servicoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<ServicoModel>> listarServicos() {
        return ResponseEntity.status(HttpStatus.OK).body(servicoRepository.findAll());
    }

    @GetMapping("/{idServico}")
    public ResponseEntity<Object> exibirservico(@PathVariable(value = "idServico") UUID id) {
        Optional<ServicoModel> servicoBuscado = servicoRepository.findById(id);

        if (servicoBuscado.isEmpty()) {
            // Retornar servico não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(servicoBuscado.get());
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarServico(@RequestBody @Valid ServicoDto servicoDto) {
        ServicoModel servicoModel = new ServicoModel();

        BeanUtils.copyProperties(servicoDto, servicoModel);

        var cliente = usuarioRepository.findById(servicoDto.id_cliente());

        if (cliente.isPresent()) {
            servicoModel.setCliente(cliente.get());
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id cliente não encontrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(servicoRepository.save(servicoModel));
    }
}

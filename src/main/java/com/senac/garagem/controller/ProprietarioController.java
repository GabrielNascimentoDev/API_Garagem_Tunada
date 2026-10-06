package com.senac.garagem.controller;

import com.senac.garagem.exception.ResourceNotFoundException;
import com.senac.garagem.model.*;
import com.senac.garagem.repository.ProprietarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/proprietarios")
@Tag(name = "Proprietarios", description = "Operações CRUD de Proprietarios")
public class ProprietarioController {

    private final ProprietarioRepository repository;
    private final PagedResourcesAssembler<Proprietario> pagedAssembler;

    public ProprietarioController(ProprietarioRepository repository, PagedResourcesAssembler<Proprietario> pagedAssembler) {
        this.repository = repository;
        this.pagedAssembler = pagedAssembler;
    }

    private EntityModel<Proprietario> toModel(Proprietario entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ProprietarioController.class).buscar(entity.getId())).withSelfRel(),
                linkTo(methodOn(ProprietarioController.class).atualizar(entity.getId(), null)).withRel("update"),
                linkTo(methodOn(ProprietarioController.class).deletar(entity.getId())).withRel("delete"),
                linkTo(ProprietarioController.class).withRel("todos"));
    }

    @GetMapping
    @Operation(summary = "Lista Proprietarios (paginado)",
            description = "Retorna uma página de resultados. Exemplo: ?page=0&size=10&sort=id,asc")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PagedModel<EntityModel<Proprietario>> listar(@ParameterObject Pageable pageable) {
        return pagedAssembler.toModel(repository.findAll(pageable), this::toModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca por ID", description = "Retorna o registro com o ID informado")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public EntityModel<Proprietario> buscar(@PathVariable Long id) {
        return toModel(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proprietario não encontrado(a) com id " + id)));
    }

    @PostMapping
    @Operation(summary = "Cria um registro", description = "Valida os dados e cria um novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso (header Location com a URI)")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<EntityModel<Proprietario>> criar(@Valid @RequestBody Proprietario body) {
        body.setId(null);
        Proprietario salvo = repository.save(body);
        URI uri = linkTo(ProprietarioController.class).slash(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(toModel(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um registro", description = "Substitui os dados do registro com o ID informado")
    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<EntityModel<Proprietario>> atualizar(@PathVariable Long id, @Valid @RequestBody Proprietario body) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Proprietario não encontrado(a) com id " + id);
        }
        body.setId(id);
        return ResponseEntity.ok(toModel(repository.save(body)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um registro", description = "Exclui o registro com o ID informado")
    @ApiResponse(responseCode = "204", description = "Removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    @ApiResponse(responseCode = "409", description = "Registro em uso por outro recurso")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Proprietario não encontrado(a) com id " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busca")
    @Operation(summary = "Busca por nome", description = "Consulta personalizada paginada filtrando por nome")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PagedModel<EntityModel<Proprietario>> buscarPorNome(@RequestParam String nome,
                                                          @ParameterObject Pageable pageable) {
        return pagedAssembler.toModel(repository.findByNomeContainingIgnoreCase(nome, pageable), this::toModel);
    }
}

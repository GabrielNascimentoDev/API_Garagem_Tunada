package com.senac.garagem.controller;

import com.senac.garagem.exception.ResourceNotFoundException;
import com.senac.garagem.model.*;
import com.senac.garagem.repository.FabricanteRepository;
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
@RequestMapping("/api/fabricantes")
@Tag(name = "Fabricantes", description = "Operações CRUD de Fabricantes")
public class FabricanteController {

    private final FabricanteRepository repository;
    private final PagedResourcesAssembler<Fabricante> pagedAssembler;

    public FabricanteController(FabricanteRepository repository, PagedResourcesAssembler<Fabricante> pagedAssembler) {
        this.repository = repository;
        this.pagedAssembler = pagedAssembler;
    }

    private EntityModel<Fabricante> toModel(Fabricante entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(FabricanteController.class).buscar(entity.getId())).withSelfRel(),
                linkTo(methodOn(FabricanteController.class).atualizar(entity.getId(), null)).withRel("update"),
                linkTo(methodOn(FabricanteController.class).deletar(entity.getId())).withRel("delete"),
                linkTo(FabricanteController.class).withRel("todos"));
    }

    @GetMapping
    @Operation(summary = "Lista Fabricantes (paginado)",
            description = "Retorna uma página de resultados. Exemplo: ?page=0&size=10&sort=id,asc")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PagedModel<EntityModel<Fabricante>> listar(@ParameterObject Pageable pageable) {
        return pagedAssembler.toModel(repository.findAll(pageable), this::toModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca por ID", description = "Retorna o registro com o ID informado")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public EntityModel<Fabricante> buscar(@PathVariable Long id) {
        return toModel(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado(a) com id " + id)));
    }

    @PostMapping
    @Operation(summary = "Cria um registro", description = "Valida os dados e cria um novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso (header Location com a URI)")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<EntityModel<Fabricante>> criar(@Valid @RequestBody Fabricante body) {
        body.setId(null);
        Fabricante salvo = repository.save(body);
        URI uri = linkTo(FabricanteController.class).slash(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(toModel(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um registro", description = "Substitui os dados do registro com o ID informado")
    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<EntityModel<Fabricante>> atualizar(@PathVariable Long id, @Valid @RequestBody Fabricante body) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fabricante não encontrado(a) com id " + id);
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
            throw new ResourceNotFoundException("Fabricante não encontrado(a) com id " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busca")
    @Operation(summary = "Busca por nome", description = "Consulta personalizada paginada filtrando por nome")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PagedModel<EntityModel<Fabricante>> buscarPorNome(@RequestParam String nome,
                                                          @ParameterObject Pageable pageable) {
        return pagedAssembler.toModel(repository.findByNomeContainingIgnoreCase(nome, pageable), this::toModel);
    }
}

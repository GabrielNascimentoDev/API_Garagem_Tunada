package com.senac.garagem.controller;

import com.senac.garagem.exception.ResourceNotFoundException;
import com.senac.garagem.model.*;
import com.senac.garagem.repository.CarroRepository;
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
@RequestMapping("/api/carros")
@Tag(name = "Carros", description = "Operações CRUD de Carros")
public class CarroController {

    private final CarroRepository repository;
    private final PagedResourcesAssembler<Carro> pagedAssembler;

    public CarroController(CarroRepository repository, PagedResourcesAssembler<Carro> pagedAssembler) {
        this.repository = repository;
        this.pagedAssembler = pagedAssembler;
    }

    private EntityModel<Carro> toModel(Carro entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CarroController.class).buscar(entity.getId())).withSelfRel(),
                linkTo(methodOn(CarroController.class).atualizar(entity.getId(), null)).withRel("update"),
                linkTo(methodOn(CarroController.class).deletar(entity.getId())).withRel("delete"),
                linkTo(CarroController.class).withRel("todos"));
    }

    @GetMapping
    @Operation(summary = "Lista Carros (paginado)",
            description = "Retorna uma página de resultados. Exemplo: ?page=0&size=10&sort=id,asc")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PagedModel<EntityModel<Carro>> listar(@ParameterObject Pageable pageable) {
        return pagedAssembler.toModel(repository.findAll(pageable), this::toModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca por ID", description = "Retorna o registro com o ID informado")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public EntityModel<Carro> buscar(@PathVariable Long id) {
        return toModel(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado(a) com id " + id)));
    }

    @PostMapping
    @Operation(summary = "Cria um registro", description = "Valida os dados e cria um novo registro")
    @ApiResponse(responseCode = "201", description = "Criado com sucesso (header Location com a URI)")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<EntityModel<Carro>> criar(@Valid @RequestBody Carro body) {
        body.setId(null);
        Carro salvo = repository.save(body);
        URI uri = linkTo(CarroController.class).slash(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(toModel(salvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um registro", description = "Substitui os dados do registro com o ID informado")
    @ApiResponse(responseCode = "200", description = "Atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<EntityModel<Carro>> atualizar(@PathVariable Long id, @Valid @RequestBody Carro body) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Carro não encontrado(a) com id " + id);
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
            throw new ResourceNotFoundException("Carro não encontrado(a) com id " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busca")
    @Operation(summary = "Busca por modelo", description = "Consulta personalizada paginada filtrando por modelo")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    public PagedModel<EntityModel<Carro>> buscarPorModelo(@RequestParam String modelo,
                                                          @ParameterObject Pageable pageable) {
        return pagedAssembler.toModel(repository.findByModeloContainingIgnoreCase(modelo, pageable), this::toModel);
    }
}

package com.senac.garagem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Fabricante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do fabricante é obrigatório")
    @Size(max = 80)
    private String nome;

    @Size(max = 60)
    private String paisOrigem;

    @JsonIgnore
    @OneToMany(mappedBy = "fabricante")
    private List<Carro> carros = new ArrayList<>();
}

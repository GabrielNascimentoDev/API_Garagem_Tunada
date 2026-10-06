package com.senac.garagem.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FichaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A potência (cv) é obrigatória")
    @Min(value = 1, message = "A potência deve ser maior que zero")
    private Integer potenciaCv;

    @Positive(message = "O torque deve ser positivo")
    private Double torqueKgfm;

    @Positive(message = "O tempo de 0 a 100 deve ser positivo")
    private Double zeroACemSegundos;

    @Min(value = 1, message = "A velocidade máxima deve ser maior que zero")
    private Integer velocidadeMaximaKmh;

    @JsonIgnore
    @OneToOne(mappedBy = "fichaTecnica")
    private Carro carro;
}

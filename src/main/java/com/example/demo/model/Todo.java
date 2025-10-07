package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "todos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private boolean completed = false;
}

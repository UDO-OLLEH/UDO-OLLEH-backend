package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "Dog")
@Entity
@Getter
@NoArgsConstructor
public class Dog {      //테스트 엔티티
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE")
    private Integer age;

    @Builder
    public Dog(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}



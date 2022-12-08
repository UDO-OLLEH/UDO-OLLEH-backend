package com.udoolleh.backend.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "ads")
@NoArgsConstructor
@Getter
@Entity
public class Ads {
    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "photo")
    private String photo;

    @Builder
    public Ads(String photo) {
        this.photo = photo;
    }

}

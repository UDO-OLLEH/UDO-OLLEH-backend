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
    @Column(name = "ads_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "photo")
    private String photo;

    @Column(name = "context")
    private String context;

    @Builder
    public Ads(String photo, String context) {
        this.photo = photo;
        this.context = context;
    }

    public void updatePhoto(String photo) {
        this.photo = photo;
    }
}

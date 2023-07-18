package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    private String type;

    @OneToMany(mappedBy = "image")
    private List<ImageTag> imageTags = new ArrayList<>();

    @Builder
    public Image(String link, String type) {
        this.link = link;
        this.type = type;
    }

    public Image() {

    }
}

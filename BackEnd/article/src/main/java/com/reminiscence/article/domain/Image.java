package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    private String type;

    @OneToMany(mappedBy = "image")
    private List<ImageTag> imageTags = new ArrayList<>();

    @OneToOne(mappedBy = "image")
    private ImageArticle imageArticle;

    @Builder
    public Image(String link, String type) {
        this.link = link;
        this.type = type;
    }

    public void addImageTag(ImageTag imageTag){
        this.imageTags.add(imageTag);
    }
}

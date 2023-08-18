package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
    private String name;

    @OneToMany(mappedBy = "image")
    private List<ImageTag> imageTags = new ArrayList<>();

    @OneToOne(mappedBy = "image")
    private ImageArticle imageArticle;

    @OneToMany(mappedBy = "image")
    private List<ImageOwner> imageOwners = new ArrayList<>();

    @Builder
    public Image(String link, String type, String name) {
        this.link = link;
        this.type = type;
        this.name = name;
    }

    public void addImageTag(ImageTag imageTag){
        this.imageTags.add(imageTag);
    }
}

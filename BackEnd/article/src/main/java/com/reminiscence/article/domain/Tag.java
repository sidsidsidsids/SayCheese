package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Tag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private List<ImageTag> imageTags = new ArrayList<>();

    public void addImageTag(ImageTag imageTag){
        this.imageTags.add(imageTag);
    }

}

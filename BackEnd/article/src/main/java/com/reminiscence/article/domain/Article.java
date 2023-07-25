package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@NoArgsConstructor
@Getter
public abstract class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @OneToMany(mappedBy = "article")
    private List<Lover> lovers = new ArrayList<>();
    public Article(Member member){

        this.member = member;
    }



}

package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;

import javax.persistence.*;
import javax.persistence.FetchType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
public abstract class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;



}

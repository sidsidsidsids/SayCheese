package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@Getter
public abstract class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "article")
    private List<Lover> lovers = new ArrayList<>();

    //생성 메서드
    public void changeMember(Member member){
        this.member = member;
    }
}

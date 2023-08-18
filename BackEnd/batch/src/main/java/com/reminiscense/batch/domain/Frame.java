package com.reminiscense.batch.domain;

import com.reminiscense.batch.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "frame")
@Getter
public class Frame extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name",nullable = false)
    private String name;
    @Column(name="link",nullable = false)
    private String link;

    @Column(name="open_yn",nullable = false)
    private Character open_yn;

    @Column(name="type",nullable = false)
    private String type;

    @Enumerated(value=EnumType.STRING)
    @Column(name="frame_specification", nullable = false)
    private FrameSpecification frameSpecification;

    @Builder
    public Frame(String name, String link, Character open_yn, String type, FrameSpecification frameSpecification) {
        this.name = name;
        this.link = link;
        this.open_yn = open_yn;
        this.type = type;
        this.frameSpecification = frameSpecification;
    }

    public void modifyOpenYn(Character open_yn){
        this.open_yn = (open_yn=='Y'?'N':'Y');
    };

}
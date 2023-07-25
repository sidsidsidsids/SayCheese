package com.reminiscence.article.domain;

import com.reminiscence.article.BaseTimeEntity;
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


}
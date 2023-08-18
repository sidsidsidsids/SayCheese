package com.reminiscense.batch.domain;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "frequently_asked_question")
public class FrequentlyAskedQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name="answer", nullable = false, columnDefinition = "TEXT")
    private String answer;
}

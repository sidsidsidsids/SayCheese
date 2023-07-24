package com.reminiscence.article.qna.vo;

import com.reminiscence.article.domain.FrequentlyAskedQuestion;
import lombok.Getter;

@Getter
public class FaqVo {
    private final Long id;
    private final String question;
    private final String answer;

    public FaqVo(FrequentlyAskedQuestion frequentlyAskedQuestion){
        this.id=frequentlyAskedQuestion.getId();
        this.question=frequentlyAskedQuestion.getQuestion();
        this.answer=frequentlyAskedQuestion.getAnswer();
    }


}

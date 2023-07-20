package com.reminiscence.article.notice.service;

import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.exception.customexception.NoticeException;
import com.reminiscence.article.exception.message.NoticeExceptionMessage;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleResponseDto;
import com.reminiscence.article.notice.repository.NoticeRepository;
import com.reminiscence.article.notice.vo.NoticeArticleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final int DEFAULT_NOTICE_PAGE_SIZE=10;
    private final NoticeRepository noticeRepository;

    @Override
    public void writeArticle(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto) {
        NoticeArticle noticeArticle = NoticeArticleAndMemberRequestDto.toEntity(noticeArticleAndMemberRequestDto);
        noticeRepository.save(noticeArticle);
    }

    @Override
    @Transactional
    public void modifyArticle(Long noticeArticleId, NoticeArticleRequestDto noticeArticleRequestDto) {
        NoticeArticle noticeArticle=noticeRepository.findById(noticeArticleId).orElseThrow(
                ()->new NoticeException(NoticeExceptionMessage.DATA_NOT_FOUND)
        );
        noticeArticle.modifySubject(noticeArticleRequestDto.getSubject());
        noticeArticle.modifyContent(noticeArticleRequestDto.getContent());
    }

    @Override
    public  NoticeArticleResponseDto getNoticeArticleList(Pageable tempPageable) {
        int page=tempPageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1,DEFAULT_NOTICE_PAGE_SIZE, Sort.Direction.DESC,"id");
        Page<NoticeArticle> noticeArticlePageList=noticeRepository.findNoticeArticle(pageable);
        NoticeArticleResponseDto noticeArticleResponseDto=new NoticeArticleResponseDto();

        noticeArticleResponseDto.setCurPage(page);
        noticeArticleResponseDto.setTotalPages(noticeArticlePageList.getTotalPages());
        noticeArticleResponseDto.setTotalDataCount(noticeArticlePageList.getTotalElements());

        for(NoticeArticle noticeArticle:noticeArticlePageList.getContent()){
            noticeArticleResponseDto.add(new NoticeArticleVo(noticeArticle));
        }
        return noticeArticleResponseDto;
    }


}

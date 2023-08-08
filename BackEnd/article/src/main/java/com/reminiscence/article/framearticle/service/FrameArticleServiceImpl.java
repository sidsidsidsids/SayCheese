package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.exception.customexception.FrameArticleException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleDeleteRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleListRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FrameArticleServiceImpl implements FrameArticleService {
    private final FrameArticleRepository frameArticleRepository;
    private final FrameRepository frameRepository;
    private final LoverRepository loverRepository;

    @Override
    public FrameArticleListResponseDto getHotFrameArticleList(Pageable tempPageable, UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto) {
        String searchWord = frameArticleListRequestDto.getSearchWord();
        int page=tempPageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1, Pagination.DEFAULT_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC,"lover");
        if(userDetail != null){
            return getMemberFrameArticleList(pageable, userDetail.getMember().getId(), searchWord);
        }else{
            return getNonMemberFrameArticleList(pageable, searchWord);
        }
    }

    @Override
    public FrameArticleListResponseDto getRecentFrameArticleList(Pageable tempPageable, UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto) {
        String searchWord = frameArticleListRequestDto.getSearchWord();
        int page=tempPageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1, Pagination.DEFAULT_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC,"createdDate");

        List<FrameArticleVo> frameArticleList;
        if(userDetail != null){
            return getMemberFrameArticleList(pageable, userDetail.getMember().getId(), searchWord);
        }else{
            return getNonMemberFrameArticleList(pageable, searchWord);
        }
    }

    @Override
    public FrameArticleListResponseDto getRandomFrameArticleList(Pageable tempPageable, UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto) {
        String searchWord = frameArticleListRequestDto.getSearchWord();
        int page=tempPageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1, Pagination.DEFAULT_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC,"random");

        List<FrameArticleVo> frameArticleList;
        if(userDetail != null){
            return getMemberFrameArticleList(pageable, userDetail.getMember().getId(), searchWord);
        }else{
            return getNonMemberFrameArticleList(pageable, searchWord);
        }
    }


    @Override
    public void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto) {
        Frame frame=FrameArticleAndMemberRequestDto.toEntity(frameArticleAndMemberRequestDto.getFrameArticleRequestDto());
        frameRepository.save(frame);
        FrameArticle frameArticle=FrameArticle.builder()
                .frame(frame)
                .member(frameArticleAndMemberRequestDto.getMember())
                .subject(frameArticleAndMemberRequestDto.getFrameArticleRequestDto().getSubject())
                .build();

        frameArticleRepository.save(frameArticle);

    }

    @Override
    public void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto) {
        FrameArticle frameArticle=frameArticleRepository.findById(frameArticleDeleteRequestDto.getFrameArticleId())
                .orElseThrow(()->new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));

        if(!frameArticle.getMember().getId().equals(frameArticleDeleteRequestDto.getMember().getId())){
            throw new FrameArticleException(FrameArticleExceptionMessage.INVALID_DELETE_AUTH);
        }
        loverRepository.deleteByArticleId(frameArticle.getId());

        frameArticleRepository.delete(frameArticle);
    }

    private FrameArticleListResponseDto getMemberFrameArticleList(Pageable page, Long memberId, String searchWord){
        Page<FrameArticleVo> frameArticlePageList = frameArticleRepository.findMemberFrameArticles(page, memberId, searchWord);
        return getFrameArticleListResponseDto(page, frameArticlePageList);
    }

    private FrameArticleListResponseDto getNonMemberFrameArticleList(Pageable page, String searchWord){
        Page<FrameArticleVo> frameArticlePageList = frameArticleRepository.findNonMemberFrameArticles(page, searchWord);
        return getFrameArticleListResponseDto(page, frameArticlePageList);
    }

    private FrameArticleListResponseDto getFrameArticleListResponseDto(Pageable page, Page<FrameArticleVo> frameArticlePageList) {
        FrameArticleListResponseDto frameArticleListResponseDto =new FrameArticleListResponseDto(page.getPageNumber(), frameArticlePageList.getTotalPages(),frameArticlePageList.getTotalElements());

        if(frameArticlePageList.getContent().size()==0) throw new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_FRAME_ARTICLE_LIST);
        for(FrameArticleVo frameArticleVo:frameArticlePageList.getContent()){

            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }
        return frameArticleListResponseDto;
    }
//    private FrameArticleListResponseDto getNonMemberFrameArticleList(Pageable page, String searchWord){
//        Optional<List<FrameArticleVo>> frameArticles = frameArticleRepository.findNonMemberFrameArticles(page, searchWord);
//        frameArticles.orElseThrow(()->
//                new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_FRAME_ARTICLE_LIST));
//        return frameArticles.get();
//    }
}

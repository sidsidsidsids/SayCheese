package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.exception.customexception.FrameArticleException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleDeleteRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import lombok.RequiredArgsConstructor;
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
    public List<FrameArticleListResponseDto> getHotFrameArticleList(UserDetail userDetail, String searchWord) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");
        if(userDetail != null){
            return getMemberFrameArticleList(page, userDetail.getMember().getId(), searchWord);
        }else{
            return getNonMemberFrameArticleList(page, searchWord);
        }
    }

    @Override
    public List<FrameArticleListResponseDto> getRecentFrameArticleList(UserDetail userDetail, String searchWord) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
        List<FrameArticleListResponseDto> frameArticleList;
        if(userDetail != null){
            frameArticleList = getMemberFrameArticleList(page, userDetail.getMember().getId(), searchWord);
        }else{
            frameArticleList = getNonMemberFrameArticleList(page, searchWord);
        }
        return frameArticleList;
    }

    @Override
    public List<FrameArticleListResponseDto> getRandomFrameArticleList(UserDetail userDetail, String searchWord) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "random");
        List<FrameArticleListResponseDto> frameArticleList;
        if(userDetail != null){
            frameArticleList = getMemberFrameArticleList(page, userDetail.getMember().getId(), searchWord);
        }else{
            frameArticleList = getNonMemberFrameArticleList(page, searchWord);
        }
        return frameArticleList;
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

    private List<FrameArticleListResponseDto> getMemberFrameArticleList(Pageable page, Long memberId, String searchWord){
        Optional<List<FrameArticleListResponseDto>> frameArticles = frameArticleRepository.findMemberFrameArticles(page, memberId, searchWord);
        frameArticles.orElseThrow(()->
                new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_FRAME_ARTICLE_LIST));
        return frameArticles.get();
    }

    private List<FrameArticleListResponseDto> getNonMemberFrameArticleList(Pageable page, String searchWord){
        Optional<List<FrameArticleListResponseDto>> frameArticles = frameArticleRepository.findNonMemberFrameArticles(page, searchWord);
        frameArticles.orElseThrow(()->
                new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_FRAME_ARTICLE_LIST));
        return frameArticles.get();
    }
}

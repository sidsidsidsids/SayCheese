package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.domain.FrameSpecification;
import com.reminiscence.article.exception.customexception.FrameArticleException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.dto.*;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.FrameResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FrameArticleServiceImpl implements FrameArticleService {
    private final FrameArticleRepository frameArticleRepository;
    private final FrameRepository frameRepository;
    private final LoverRepository loverRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    @Value("${cloud.aws.region.static}")
    private String BUCKET_REGION;

    @Override
    public FrameArticleListResponseDto getHotFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec) {
        int page = tempPageable.getPageNumber();
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, Pagination.DEFAULT_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC, "lover");

        if (userDetail != null) {
            return getMemberFrameArticleList(pageable, userDetail.getMember().getId(), searchWord, frameSpec);
        } else {
            return getNonMemberFrameArticleList(pageable, searchWord, frameSpec);
        }
    }

    @Override
    public FrameArticleListResponseDto getRecentFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec) {
        int page = tempPageable.getPageNumber();
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, Pagination.DEFAULT_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC, "createdDate");

        if (userDetail != null) {
            return getMemberFrameArticleList(pageable, userDetail.getMember().getId(), searchWord, frameSpec);
        } else {
            return getNonMemberFrameArticleList(pageable, searchWord, frameSpec);
        }
    }

    @Override
    public FrameArticleListResponseDto getRandomFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec) {
        int page = tempPageable.getPageNumber();
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, Pagination.DEFAULT_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC, "random");

        if (userDetail != null) {
            return getMemberFrameArticleList(pageable, userDetail.getMember().getId(), searchWord, frameSpec);
        } else {
            return getNonMemberFrameArticleList(pageable, searchWord, frameSpec);
        }
    }

    @Override
    public FrameArticleListResponseDto getMyFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec) {
        int page = tempPageable.getPageNumber();
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, Pagination.DEFAULT_MY_FRAME_PER_PAGE_SIZE, Sort.Direction.DESC, "createdDate");

        Page<FrameArticleVo> frameArticlePageList = frameArticleRepository.findMyFrameArticles(pageable, userDetail.getMember().getId(), searchWord, frameSpec);

        return getFrameArticleListResponseDto(pageable, frameArticlePageList);
    }

    @Transactional
    @Override
    public Response alterPublicStatusFrameArticle(FrameArticleAlterPublicRequestDto frameArticleAlterPublicRequestDto) {
        FrameArticle frameArticle = frameArticleRepository.findById(frameArticleAlterPublicRequestDto.getFrameArticleId())
                .orElseThrow(() -> new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));

        if (!frameArticle.getMember().getId().equals(frameArticleAlterPublicRequestDto.getMember().getId())) {
            throw new FrameArticleException(FrameArticleExceptionMessage.INVALID_MODIFY_AUTH);
        }

        Frame frame = frameRepository.findById(frameArticle.getFrame().getId()).orElseThrow(() -> new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));

        frame.modifyOpenYn(frame.getOpen_yn());

        if (frame.getOpen_yn() == 'Y')
            return Response.of(FrameResponseMessage.FRAME_MODIFY_PUBLIC_SUCCESS);
        else
            return Response.of(FrameResponseMessage.FRAME_MODIFY_NOT_PUBLIC_SUCCESS);
    }

    @Override
    public void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto) {
        StringBuilder link = new StringBuilder();
        link.append("https://")
                .append(BUCKET_NAME)
                .append(".s3.")
                .append(BUCKET_REGION)
                .append(".amazonaws.com/")
                .append(frameArticleAndMemberRequestDto.getFrameArticleRequestDto().getFileType())
                .append("/")
                .append(frameArticleAndMemberRequestDto.getFrameArticleRequestDto().getName());
        Frame frame = FrameArticleAndMemberRequestDto.toEntity(frameArticleAndMemberRequestDto.getFrameArticleRequestDto(), link.toString());
        frameRepository.save(frame);
        FrameArticle frameArticle = FrameArticle.builder()
                .frame(frame)
                .member(frameArticleAndMemberRequestDto.getMember())
                .subject(frameArticleAndMemberRequestDto.getFrameArticleRequestDto().getSubject())
                .build();

        frameArticleRepository.save(frameArticle);
    }

    @Transactional
    @Override
    public void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto) {
        FrameArticle frameArticle = frameArticleRepository.findById(frameArticleDeleteRequestDto.getFrameArticleId())
                .orElseThrow(() -> new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));

        if (!frameArticle.getMember().getId().equals(frameArticleDeleteRequestDto.getMember().getId())) {
            throw new FrameArticleException(FrameArticleExceptionMessage.INVALID_DELETE_AUTH);
        }
        loverRepository.deleteByArticleId(frameArticle.getId());

        frameArticleRepository.delete(frameArticle);
    }

    @Override
    public FrameArticleResponseDto readFrameArticle(FrameArticleReadRequestDto frameArticleReadRequestDto) {
        // 회원일 때
        if (frameArticleReadRequestDto.getUserDetail() != null) {
            FrameArticleVo frameArticleVo = frameArticleRepository.findMemberFrameArticle(frameArticleReadRequestDto);
            if(frameArticleVo == null) throw new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA);
            return new FrameArticleResponseDto(frameArticleVo);
        }

        // 비회원일 때
        FrameArticle frameArticle = frameArticleRepository.findById(frameArticleReadRequestDto.getFrameArticleId()).orElseThrow(() -> new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));

        /* 속성 값 순서
        Long articleId
        String subject
        String frameLink
        Long loverCnt
        LocalDateTime createdDate
        String author
        Character open_yn
        FrameSpecification frameSpecification
        */
        FrameArticleResponseDto frameArticleResponseDto = new FrameArticleResponseDto(
                frameArticle.getId(),
                frameArticle.getSubject(),
                frameArticle.getFrame().getLink(),
                (long) frameArticle.getLovers().size(),
                frameArticle.getCreatedDate(),
                frameArticle.getMember().getNickname(),
                frameArticle.getFrame().getOpen_yn(),
                frameArticle.getFrame().getFrameSpecification()
        );
        return frameArticleResponseDto;
    }


    private FrameArticleListResponseDto getMemberFrameArticleList(Pageable pageable, Long memberId, String searchWord, String frameSpec) {
        Page<FrameArticleVo> frameArticlePageList = frameArticleRepository.findMemberFrameArticles(pageable, memberId, searchWord, frameSpec);
        return getFrameArticleListResponseDto(pageable, frameArticlePageList);
    }

    private FrameArticleListResponseDto getNonMemberFrameArticleList(Pageable pageable, String searchWord, String frameSpec) {
        Page<FrameArticleVo> frameArticlePageList = frameArticleRepository.findNonMemberFrameArticles(pageable, searchWord, frameSpec);
        return getFrameArticleListResponseDto(pageable, frameArticlePageList);
    }

    private FrameArticleListResponseDto getFrameArticleListResponseDto(Pageable pageable, Page<FrameArticleVo> frameArticlePageList) {
        int page = pageable.getPageNumber() + 1;
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(page, frameArticlePageList.getTotalPages(), frameArticlePageList.getTotalElements());

//        if(frameArticlePageList.getContent().size()==0) throw new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_FRAME_ARTICLE_LIST);
        for (FrameArticleVo frameArticleVo : frameArticlePageList.getContent()) {

            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }
        return frameArticleListResponseDto;
    }
}

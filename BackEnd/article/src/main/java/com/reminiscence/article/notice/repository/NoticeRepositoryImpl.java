package com.reminiscence.article.notice.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.domain.QMember;
import com.reminiscence.article.domain.QNoticeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    public NoticeRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<NoticeArticle> findNoticeArticle(Pageable pageable) {
        List<NoticeArticle> list= jpaQueryFactory.selectFrom(QNoticeArticle.noticeArticle)
                                                .join(QNoticeArticle.noticeArticle.member,QMember.member)
                                                .orderBy(QNoticeArticle.noticeArticle.id.desc())
                                                .offset(pageable.getOffset())
                                                .limit(pageable.getPageSize())
                                                .fetchJoin()
                                                .fetch();

        Long count =  jpaQueryFactory.select(Wildcard.count)
                .from(QNoticeArticle.noticeArticle)
                .fetchOne();


        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Optional<NoticeArticle> findNoticeArticleById(Long id) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(QNoticeArticle.noticeArticle)
                .join(QNoticeArticle.noticeArticle.member, QMember.member)
                .where(QNoticeArticle.noticeArticle.id.eq(id))
                .fetchJoin()
                .fetchOne());
    }
}

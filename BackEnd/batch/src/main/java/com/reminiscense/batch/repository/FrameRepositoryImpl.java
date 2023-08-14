package com.reminiscense.batch.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscense.batch.domain.Frame;
import com.reminiscense.batch.domain.FrameArticle;
import com.reminiscense.batch.domain.QFrame;
import com.reminiscense.batch.domain.QFrameArticle;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class FrameRepositoryImpl implements FrameRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    public FrameRepositoryImpl(EntityManager entityManager){
        jpaQueryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Frame> findByDeletedFrame() {
        return jpaQueryFactory.selectFrom(QFrame.frame)
                .leftJoin(QFrameArticle.frameArticle).on(QFrame.frame.eq(QFrameArticle.frameArticle.frame))
                .where(QFrameArticle.frameArticle.isNull())
                .fetch();
    }

    @Override
    public void deleteFrames(List<Frame> frameList) {
        if(frameList.size()<=0){ // 삭제할 데이터가 존재하지 않는다면
            return;
        }
        jpaQueryFactory.delete(QFrame.frame)
                .where(inFrameId(frameList))
                .execute();
    }
    public BooleanExpression inFrameId(List<Frame> frameList){
        if(frameList.size()==0){
            return null;
        }
        return QFrame.frame.in(frameList);
    }
}

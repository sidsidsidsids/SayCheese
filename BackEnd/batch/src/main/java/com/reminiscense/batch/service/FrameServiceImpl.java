package com.reminiscense.batch.service;

import com.amazonaws.services.s3.AmazonS3;
import com.reminiscense.batch.domain.FileType;
import com.reminiscense.batch.domain.Frame;
import com.reminiscense.batch.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameServiceImpl implements FrameService {
    private final FrameRepository frameRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    @Override
    public ExitStatus removeFrame() {
        String prefix= FileType.FRAME.getValue();
        List<Frame> frameList=frameRepository.findByDeletedFrame();
        List<Frame> deleteFrameList=new ArrayList<>();
        for(Frame frame : frameList){
            try{
                String key=prefix+"/"+frame.getName()+"."+frame.getType();
                amazonS3.deleteObject(BUCKET_NAME,key);
                deleteFrameList.add(frame);
            }catch(Exception e){
                log.error("삭제 실패 {}",frame.getLink());
            }
        }
        frameRepository.deleteFrames(deleteFrameList);

        return ExitStatus.COMPLETED;
    }

}

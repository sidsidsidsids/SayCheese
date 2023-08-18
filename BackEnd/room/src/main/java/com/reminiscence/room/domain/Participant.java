package com.reminiscence.room.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="owner_yn")
    private Character ownerYn;
    @Column(name = "stream_id")
    private String streamId;
    @Column(name = "connection_yn")
    private Character connectionYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Builder
    public Participant(Character ownerYn, Room room, Member member, String streamId, Character connectionYn) {
        this.ownerYn = ownerYn;
        this.room = room;
        this.member = member;
        this.streamId = streamId;
        this.connectionYn = connectionYn;
    }

    public void updateOwnerYn(Character ownerYn){
        this.ownerYn = ownerYn;
    }

    public void updateStreamId(String streamId){
        this.streamId = streamId;
    }
    public void updateConnectionYn(Character connectionYn){
        this.connectionYn = connectionYn;
    }

}

package com.reminiscence.room.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="password")
    private String password;
    @Column(name="max_count")
    private int maxCount;
    @Column(name="specification")
    @Enumerated(EnumType.STRING)
    private Specification specification;
    @Column(name="start_yn")
    private Character startYn;
    @Column(name="mode")
    @Enumerated(EnumType.STRING)
    private Mode mode;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "room_code")
    private String roomCode;
    @OneToMany(mappedBy = "room")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Room(String password, int maxCount, Specification specification,
                Mode mode, LocalDateTime endDate, String roomCode, Character startYn){
        this.password = password;
        this.maxCount = maxCount;
        this.specification = specification;
        this.mode = mode;
        this.endDate = endDate;
        this.roomCode = roomCode;
        this.startYn = startYn;
    }

    public void updateRoomStart(){
        this.startYn = 'Y';
    }
}

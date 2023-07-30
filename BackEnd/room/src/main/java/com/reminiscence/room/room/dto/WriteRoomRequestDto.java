package com.reminiscence.room.room.dto;

import com.reminiscence.room.domain.Mode;
import com.reminiscence.room.domain.Room;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
public class WriteRoomRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @Size(min = 1, max = 4, message = "최대 인원수는 1명 이상 4명 이하입니다.")
    private int maxCount;
    @NotBlank(message = "모드를 선택해주세요.")
    private Mode mode;
    @NotBlank(message = "방 코드를 입력해주세요.")
    private String roomCode;
    @NotBlank(message = "방 설명을 입력해주세요.")
    private String specification;

    public Room toEntity() {
        return Room.builder()
                .password(password)
                .maxCount(maxCount)
                .mode(mode)
                .roomCode(roomCode)
                .specification(specification)
                .endDate(LocalDateTime.now().plusMinutes(30))
                .build();
    }
}

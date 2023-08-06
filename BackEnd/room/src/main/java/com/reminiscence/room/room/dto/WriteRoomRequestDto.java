package com.reminiscence.room.room.dto;

import com.reminiscence.room.domain.Mode;
import com.reminiscence.room.domain.Room;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
public class WriteRoomRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @Min(value = 1, message = "최소 인원은 1명입니다.")
    @Max(value = 4, message = "최대 인원은 4명입니다.")
    private Integer maxCount;
    @NotNull(message = "모드를 선택해주세요.")
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

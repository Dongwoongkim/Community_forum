package dongwoongkim.springbootboard.dto.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.dto.member.MemberDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private Long id;
    private String content;
    private MemberDto sender;
    private MemberDto receiver;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static MessageDto toDto(Message message) {
        return new MessageDto(message.getId(),
                message.getContent(),
                MemberDto.toDto(message.getSendMember()),
                MemberDto.toDto(message.getReceiveMember()),
                message.getCreatedTime()
        );
    }
}

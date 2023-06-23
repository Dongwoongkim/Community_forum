package dongwoongkim.springbootboard.dto.message;

import dongwoongkim.springbootboard.domain.message.Message;
import dongwoongkim.springbootboard.exception.member.MemberNotFoundException;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@ApiModel(value = "쪽지 생성 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDto {

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "수신자 아이디", notes = "수신자 아이디를 입력해 주세요", example = "1")
    @NotNull(message = "수신자 아이디를 입력해 주세요")
    @Positive(message = "올바른 수신자 아이디를 입력해주세요")
    private Long receiveMemberId;

    @ApiModelProperty(value = "쪽지 내용", notes = "쪽지 내용을 입력해주세요", example = "hi")
    @NotBlank(message = "쪽지 내용을 입력해주세요")
    private String content;

    public static Message toEntity(MessageCreateRequestDto messageCreateRequestDto, MemberRepository memberRepository) {
        return new Message(messageCreateRequestDto.getContent(),
                memberRepository.findById(messageCreateRequestDto.getMemberId()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findById(messageCreateRequestDto.getReceiveMemberId()).orElseThrow(MemberNotFoundException::new));
    }
}

package dongwoongkim.springbootboard.dto.alarm;

import dongwoongkim.springbootboard.dto.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmInfoDto {
    private MemberDto memberDto;
    private String message;
}

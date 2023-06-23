package dongwoongkim.springbootboard.dto.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDtoList {

    private int numberOfElements;
    private boolean hasNext;
    private List<MessageSimpleDto> messageList;

    public static MessageDtoList toDto(Slice<MessageSimpleDto> slice) {
        return new MessageDtoList(slice.getNumberOfElements(), slice.hasNext(), slice.getContent());
    }
}

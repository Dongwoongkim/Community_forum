package dongwoongkim.springbootboard.controller.message;

import dongwoongkim.springbootboard.aop.AssignMemberId;
import dongwoongkim.springbootboard.dto.message.MessageCreateRequestDto;
import dongwoongkim.springbootboard.dto.message.MessageDtoList;
import dongwoongkim.springbootboard.dto.message.MessageReadCondition;
import dongwoongkim.springbootboard.dto.response.Response;
import dongwoongkim.springbootboard.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Message Controller", tags = "Message")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @ApiOperation(value = "보낸 메시지 조회", notes = "보낸 메시지를 조회합니다.")
    @GetMapping("/send")
    @ResponseStatus(value = HttpStatus.OK)
    @AssignMemberId
    public Response readAllSendMessage(@Valid MessageReadCondition cond) {
        return Response.success(messageService.readAllSendMessage(cond));
    }

    @ApiOperation(value = "받은 메시지 조회", notes = "받은 메시지를 조회합니다.")
    @GetMapping("/receive")
    @ResponseStatus(value = HttpStatus.OK)
    @AssignMemberId
    public Response readAllReceiveMessage(@Valid MessageReadCondition cond) {
        return Response.success(messageService.readAllReceiveMessage(cond));
    }

    @ApiOperation(value = "쪽지 조회", notes = "쪽지를 조회한다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@ApiParam(value = "쪽지 id", required = true) @PathVariable Long id) {
        return Response.success(messageService.read(id));
    }

    @ApiOperation(value = "메시지 생성", notes = "메시지를 생성합니다.")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @AssignMemberId
    public Response createMessage(@Valid @RequestBody MessageCreateRequestDto messageCreateRequestDto) {
        messageService.create(messageCreateRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "송신자의 메시지 삭제", notes = "송신자의 메시지를 삭제합니다.")
    @DeleteMapping("/send/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response deleteBySender(@ApiParam(value = "보낸 메시지 중 삭제할 쪽지 ID") @PathVariable Long id) {
        messageService.deleteBySender(id);
        return Response.success();
    }

    @ApiOperation(value = "수신자의 메시지 삭제", notes = "받은 메시지를 삭제합니다.")
    @DeleteMapping("/receive/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response deleteByReceive(@ApiParam(value = "받은 메시지 중 삭제할 쪽지 ID") @PathVariable Long id) {
        messageService.deleteByReceiver(id);
        return Response.success();
    }

}

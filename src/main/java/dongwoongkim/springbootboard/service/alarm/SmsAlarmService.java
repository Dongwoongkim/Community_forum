package dongwoongkim.springbootboard.service.alarm;

import dongwoongkim.springbootboard.dto.alarm.AlarmInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsAlarmService implements AlarmService {
    @Override
    public void alarm(AlarmInfoDto alarmInfoDto) {
        log.info("{} 에게 문자 알림 전송 = {} ", alarmInfoDto.getMemberDto(), alarmInfoDto.getMessage());
    }
}

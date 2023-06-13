package dongwoongkim.springbootboard.aop;

import dongwoongkim.springbootboard.helper.AuthHelper;
import dongwoongkim.springbootboard.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect // Aspect임을 지정하고,
@Component // 스프링 컨테이너에 등록
@RequiredArgsConstructor
@Slf4j
public class AssignMemberIdAspect {

    private final AuthHelper authHelper;

    @Before("@annotation(dongwoongkim.springbootboard.aop.AssignMemberId)") // 부가 기능이 수행되는 지점
    // @AssignMemberId 어노테이션이 적용된 메소드들은, 본래의 메소드 수행 직전 assignMemberId()가 수행됨.
    public void assignMemberId(JoinPoint joinPoint) {
        // JoinPoint.getArgs()를 이용하여 전달되는 인자들을 확인하고, setMemberId 메소드가 정의된 타입이 있다면, memberId을 주입해줍니다.
        log.info("joinPoint = {}", joinPoint.getArgs());
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> getMethod(arg.getClass(), "setMemberId")
                        .ifPresent(setMemberId -> invokeMethod(arg, setMemberId, authHelper.extractMemberId())));

        log.info("finded!");
    }

    // JointPoint에서 method 조회
    private Optional<Method> getMethod(Class<?> clazz, String methodName) { // 6
        try {
            return Optional.of(clazz.getMethod(methodName, Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    // 리플렉션 API에서 발생하는 checked exception에 대한 처리
    private void invokeMethod(Object obj, Method method, Object... args) { // 7
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}

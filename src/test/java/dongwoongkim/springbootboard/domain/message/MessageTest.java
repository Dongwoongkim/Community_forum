package dongwoongkim.springbootboard.domain.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static dongwoongkim.springbootboard.factory.message.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    tdd

    @Test
    void deleteBySenderTest() {
        Message message = createMessage();
        message.isDeletedBySender()

    }

    @Test
    void deleteByReceiverTest() {
    }

    @Test
    void isDeletableTest() {
        // given
        Message message = createMessage();
        message.deleteBySender();
        message.deleteByReceiver();

        // when
        boolean deletable = message.isDeletable();

        // then
        assertThat(deletable).isTrue();
    }

}
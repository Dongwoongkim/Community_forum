package dongwoongkim.springbootboard.domain.message;

import org.junit.jupiter.api.Test;

import static dongwoongkim.springbootboard.factory.message.MessageFactory.createMessage;
import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    void deleteBySenderTest() {
        Message message = createMessage();
        message.deleteBySender();
        assertThat(message.isDeletedBySender()).isTrue();
    }

    @Test
    void deleteByReceiverTest() {
        Message message = createMessage();
        message.deleteByReceiver();
        assertThat(message.isDeletedByReceiver()).isTrue();
    }

    @Test
    public void isNotDeletableTest() throws Exception {
        //given
        Message message = createMessage();

        // then
        assertThat(message.isDeletable()).isFalse();
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
package databaseTutorial;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageRepositoryTest {

    MessageRepository messageRepository = new MessageRepository();

    @BeforeClass
    public static void createDatabase() {
        MessageRepository.createTable();
    }

    @Test
    public void shouldCreate() {
        String userName = "Markku Lavi";
        Author testAuthor = messageRepository.create(new Author(userName));
        assertEquals(userName, testAuthor.getUserName());
        assertNotEquals("Id must not be zero",0, testAuthor.getId());
        assertEquals("messagecount must be 1 after creation as user will be put into database at first message", 1, testAuthor.getMessageCount());
        }

    @Test
    public void shouldUpdate() {
        Author testUserUpdate = messageRepository.create(new Author("Marko Närhi"));
        int idOriginal = testUserUpdate.getId();
        String nameOriginal = testUserUpdate.getUserName();
        int mesCountOriginal = testUserUpdate.getMessageCount();
        messageRepository.update(testUserUpdate);
        assertTrue(testUserUpdate.getMessageCount() > mesCountOriginal);
        assertEquals(testUserUpdate.getId(), idOriginal);
        assertEquals(testUserUpdate.getUserName(), nameOriginal);

        messageRepository.printAll();
    }
}

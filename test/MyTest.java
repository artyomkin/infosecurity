import DAO.FileRepo;
import algorithms.aes.AES;
import exceptions.DecryptionFailedException;
import exceptions.InvalidUsernameException;
import org.junit.jupiter.api.Test;
import service.UserService;
import structures.Array;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTest {
    @Test
    public void test() throws DecryptionFailedException, IOException, InvalidUsernameException {
        FileRepo fileRepo = new FileRepo("hello/test", "1234567890123456");
        fileRepo.readFromFile("hello/test/0001");

        fileRepo.flushToDisk();
    }
}
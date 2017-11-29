package beans.services.json;

import beans.models.User;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class JsonHandlerTest {

    private JsonHandler<User> jsonHandler = new JsonHandler<User>(User.class);

    @Test
    public void getUserFromJsonTest() throws Exception {
        File file = new File("users.json");
        String contentType = "text/plain";
        byte[] content = null;
        content = readAllBytes(get(file.getPath()));
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), contentType, content);
        jsonHandler.getObjectFromFile(multipartFile);
    }
}
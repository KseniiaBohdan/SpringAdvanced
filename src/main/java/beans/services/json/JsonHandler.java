package beans.services.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHandler<T> {

    private final Class<T> type;

    public JsonHandler(Class<T> type) {
        this.type = type;
    }

    public T getObjectFromFile(MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T object = mapper.readValue(read(file), type);
        return object;
    }

    private String read(MultipartFile multipartFile) throws IOException {
        StringBuilder result = new StringBuilder();
        String line;
        InputStream inputStream = multipartFile.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        inputStream.close();
        bufferedReader.close();
        return result.toString();
    }

}

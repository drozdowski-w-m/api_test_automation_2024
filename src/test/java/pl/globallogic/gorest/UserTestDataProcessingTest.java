package pl.globallogic.gorest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.core.Ja
import com.github.javafaker.Faker;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.globallogic.gorest.dto.UserRequestDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserTestDataProcessingTest {

    List<UserRequestDto> users;
    ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(UserTestDataProcessingTest.class);

    @BeforeClass
    public void testSetUp() throws IOException {
        Path source = Path.of("src/test/resources/users.csv");
        users = loadUsersFromFile(source);
    }

    @Test
    public void shouldLoadUserObjectFromCSV() {
        String expectedName = "Wojciech Drozdowski";
        Assert.assertEquals(expectedName, users.getFirst().name());
    }

    @Test
    public void shouldSerializeUserObjectIntoJSON() throws JsonProcessingException {
        String firstUserAsString = mapper.writeValueAsString(users.getFirst());
        logger.info("Serialized user: {}", firstUserAsString);
        Assert.assertTrue(firstUserAsString.contains("Wojciech"));
    }

    @Test
    public void shouldDeserializeUserObjectFromJSON() throws JsonProcessingException {
        var user = """
            {"name":"Wojciech Drozdowski","email":"w.d@gmail.com","gender":"male","status":"active"}
            """;
        UserRequestDto firstUser = mapper.readValue(user, UserRequestDto.class);
        logger.info("Deserialized user: {}", firstUser);
        Assert.assertTrue(firstUser.name().contains("Wojciech"));
    }

    private List<UserRequestDto> loadUsersFromFile(Path source) throws IOException {
        return Files.readAllLines(source)
                .stream()
                .map(this::parseUser)
                .toList();
    }

    private UserRequestDto parseUser(String rawData){
        var tokens = rawData.split(",");
        if (tokens.length == 4)
            return new UserRequestDto(tokens[0], randomiseEmail(tokens[1]), tokens[2], tokens[3]);
        return new UserRequestDto("Dummy user", "dummy.email@mail.com", "male", "inactive");
    }

    private String randomiseEmail(String email) {
        String[] emailTokens = email.split("@");
        String bigLebowskiPersona = Faker.instance().lebowski().character();
        String randomPart = RandomStringUtils.randomAlphanumeric(4) + "@";
        return bigLebowskiPersona + randomPart + emailTokens[1];//emailTokens[0] + randomPart + emailTokens[1];
    }

    /*private UserRequestDto parseUser(String rawData){
        String[] r = rawData.split(",");
        UserRequestDto result = new UserRequestDto(r[0], r[1], r[2], r[3]);
        return result;
    }*/

}

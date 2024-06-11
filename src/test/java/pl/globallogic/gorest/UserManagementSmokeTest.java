package pl.globallogic.gorest;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.globallogic.gorest.api.UserApi;
import pl.globallogic.gorest.dto.UserRequestDto;
import pl.globallogic.gorest.dto.UserResponseDto;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;


import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class UserManagementSmokeTest {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementSmokeTest.class);
    private static final String TOKEN = System.getProperty("token");
    private UserApi userApi;


    @BeforeMethod
    public void testSetUp(){
        userApi = new UserApi(TOKEN);
    }

    @AfterMethod
    public void testCleanUp() {
        logger.info("Put your test clean up activities here!!");
    }

    @Test
    public void shouldListAllUsersInformationWithPaginationOptions() {
        int user_per_page = 10;
        int target_page = 3;
        List<UserResponseDto> users = userApi.getUsers(target_page,user_per_page);
        Assert.assertNotNull(users.getFirst().id());

    }

    @Test
    public void shouldCreateUserAndReturnId() {
        UserRequestDto payload = getUserDataWithRandomEmail();
        UserResponseDto newUser = userApi.createUser(payload);
        logger.info("Id for new user is '{}'", newUser.id());
        Assert.assertNotNull(newUser.id());
    }

    @Test
    public void shouldGetUserInformationById() {
        var user = userApi.createUser(getUserDataWithRandomEmail());
        UserResponseDto userFromGet = userApi.getUser(user.id());
        Assert.assertEquals(user.name(), userFromGet.name());
    }

    @Test
    public void shouldUpdateUserWithNewEmail() {
        var user = userApi.createUser(getUserDataWithRandomEmail());
        String newEmail = "some_email%s@gmail.pl".formatted(RandomStringUtils.randomAlphanumeric(5));
        logger.info("Generated new user email: {}", newEmail);
        UserRequestDto updatedUserPayload = new UserRequestDto(user.name(), newEmail, user.gender(), user.status());
        UserResponseDto updateResponse = userApi.updateUser(user.id(), updatedUserPayload);
        Assert.assertEquals(newEmail, updateResponse.email());
    }

    @Test
    public void shouldDeleteUserFromSystem() {
        var user = userApi.createUser(getUserDataWithRandomEmail());
        userApi.deleteUser(user.id());
        userApi.getResponse().then().assertThat().statusCode(204);
    }

    @Test
    public void validateUserDataAgainstSchema() {
        var user = userApi.createUser(getUserDataWithRandomEmail());
        userApi.validateResponseAgainstSchema(user.id(), "user_schema.json");
    }

    private UserRequestDto getUserDataWithRandomEmail() {
        String randomEmailSuffix = RandomStringUtils.randomAlphanumeric(5);
        UserRequestDto payload = new UserRequestDto(
                "Wojciech Drozdowski",
                "wd%s@gmail.com".formatted(randomEmailSuffix),
                "male",
                "active");
        return payload;
    }






  // before refactor

    /*private static final String HOST = "https://gorest.co.in/public/v2";
    private static final String ENDPOINT = "/users";
    private static final Logger logger = LoggerFactory.getLogger(UserManagementSmokeTest.class);
    private static final String ENDPOINT_WITH_ID = "/users/{userId}";
    private static final String TOKEN = System.getProperty("token"); //"edb4afe94ffa5dd5ee817c3eb2ff3a0408777f6781098e96969cd9cb742050d4";
    private UserApi userApi;
    private RequestSpecification req;

    @BeforeMethod
    public void testSetUp(){
        userApi = new UserApi(TOKEN);
        req = given()
                .basePath(ENDPOINT)
                .baseUri(HOST)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TOKEN);
    }

    @AfterMethod
    public void testCleanup(){
        logger.info("Put cleanup test");
    }

    private UserResponseDto createUser(UserRequestDto userData){
        RequestSpecification req = given()
                .basePath(ENDPOINT)
                .baseUri(HOST)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TOKEN) //System.getProperty("token"))
                .body(userData);
        Response response = req.post().andReturn();
        logger.info("Created user: {}", response.prettyPrint());
        return response.then().extract().as(UserResponseDto.class);
    }

    private UserRequestDto getUserDataWithRandomEmail() {
        String randomEmailSuffix = RandomStringUtils.randomAlphanumeric(5);
        UserRequestDto payload = new UserRequestDto(
                "Wojciech Drozdowski",
                "w.drozd%s@gmail.com".formatted(randomEmailSuffix),
                "male",
                "active");
        return payload;
    }

    @Test
    public void shouldListAllUsersInformation(){
        given() //arange
                .baseUri(HOST)
                .basePath(ENDPOINT)
                .queryParam("page", 3)
                .queryParam("per_page", 10)
                .log()
                .parameters()
        .when() //act
                .get()
        .then() //assert
        .log()
                .body()  //body||headers || all
                .statusCode(200);
    }

    @Test
    public void listAllWithPagination(){
        int user_per_page = 10;
        int target_page = 3;
        *//*var users = req
                .queryParam("page", target_page)
                .queryParam("per_page", user_per_page)
        .when()
                .get()
        .then()
                .statusCode(200)
                .header("x-pagination-limit", equalTo(String.valueOf(user_per_page)))
                .body("[0].id", notNullValue());*//*

        Assert.assertNotNull(users.getFirst().id());
    }

    @Test
    public void shouldCreateUserAndReturnId(){
        String randomEmailPrefix = RandomStringUtils.randomAlphanumeric(5);
    // homework: pass the payload as a string
        *//*Map<String, String> payload = Map.of(
            "name", "Wojciech Drozdowski",
                "email","wojciech.drozdowski%s@gmail.com".formatted(randomEmailPrefix),
                "gender", "male",
                "status","active"
        );*//*
        UserRequestDto payload = new UserRequestDto(
                "Wojciech Drozdowski",
                "wojciech.drozdowski%s@gmail.com".formatted(randomEmailPrefix),
                "male",
                "active"
        ); //to jest data storage read only - java record

        RequestSpecification req = given()
                .baseUri(HOST)
                .basePath(ENDPOINT)
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer edb4afe94ffa5dd5ee817c3eb2ff3a0408777f6781098e96969cd9cb742050d4")
                .body(payload);
        Response response = req.post().andReturn();
        UserResponseDto newUser = response.then().extract().as(UserResponseDto.class);
        //get id from respone
        //String id = response.jsonPath().getString("id");
        //logger.info("Id for new user is {}", id);
        logger.info("Id for new user is {}", newUser.id());
        Assert.assertNotNull(newUser.id());
    }

    *//*@Test
    public void shouldGetUserInformationById(){
        //define expected user name
        // add create user call here and extract the id
        RequestSpecification req = given()
                .baseUri(HOST)
                .basePath(ENDPOINT+"/{userId}")
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer edb4afe94ffa5dd5ee817c3eb2ff3a0408777f6781098e96969cd9cb742050d4");
        req.pathParam("userId","6927414");

        // req.get().then().log().body();
        //send request
        //get response
        //verify user name as expected
    }*//*
    @Test
    public void shoudVerifyUserByName(){
        String randomEmailPrefix = RandomStringUtils.randomAlphanumeric(5);
        UserRequestDto payload = new UserRequestDto(
                "Woj Drozdowski",
                "wojciech.drozdowski%s@gmail.com".formatted(randomEmailPrefix),
                "male",
                "active"
        );
        RequestSpecification req = given()
                .baseUri(HOST)
                .basePath(ENDPOINT)
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer edb4afe94ffa5dd5ee817c3eb2ff3a0408777f6781098e96969cd9cb742050d4")
                .body(payload);

        Response response = req.post().andReturn();
        UserResponseDto newUser = response.then().extract().as(UserResponseDto.class);
        logger.info("Name for new user is {}", newUser.name());
        Assert.assertEquals(payload.name(), newUser.name());

    }

    @Test
    public void shouldGetUserInformationById() {
        var user = createUser(getUserDataWithRandomEmail());
        RequestSpecification req = given()
                .basePath(ENDPOINT_WITH_ID)
                .baseUri(HOST)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .pathParam("userId", user.id());
        UserResponseDto userFromGet = req.get().then().extract().as(UserResponseDto.class);
        Assert.assertEquals(user.name(), userFromGet.name());
    }

    @Test
    public void shouldUpdateUserWithNewEmail() {
        var user = createUser(getUserDataWithRandomEmail());
        String newEmail = "some_email%s@gmail.pl".formatted(RandomStringUtils.randomAlphanumeric(5));
        logger.info("Generated new user email: {}", newEmail);
        RequestSpecification req = given()
                .basePath(ENDPOINT_WITH_ID)
                .baseUri(HOST)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .pathParam("userId", user.id());
        UserRequestDto updatedUserPayload = new UserRequestDto(
                user.name(), newEmail, user.gender(), user.status()
        );
        UserResponseDto updateResponse = req.body(updatedUserPayload).put().as(UserResponseDto.class);
        logger.info("User after update: {}", updateResponse);
        Assert.assertEquals(newEmail, updateResponse.email());
    }

    @Test
    public void validateUserDataAgainstSchema() {
        var user = createUser(getUserDataWithRandomEmail());
        logger.info("Validating user against schema '{}'", "user_schema.json");
        req.basePath(ENDPOINT_WITH_ID).pathParam("userId", user.id());
        req.get().then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("user_schema.json"));
    }*/

}

package services.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.pojo.requests.CreateBookingRequest;
import services.pojo.requests.CreateTokenRequest;
import services.pojo.requests.PartialUpdateBookingRequest;
import services.pojo.requests.UpdateBookingRequest;
import services.pojo.responses.CreateBookingResponse;
import services.pojo.responses.CreateTokenResponse;
import services.pojo.responses.GetBookingIdsResponse;
import services.pojo.responses.UpdateBookingResponse;
import utils.SharedData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ApiCaseSteps {

    public static class HotelBookingApiTests {

        protected Logger logger = LoggerFactory.getLogger(getClass());

        Random random = new Random();

        private final String BASE_URL = "http://restful-booker.herokuapp.com";
        private final String TOKEN_USERNAME = "admin";
        private final String TOKEN_PASSWORD = "password123";
        private final String CHECK_IN_DATE = calculateCurrentDate(10);
        private final String CHECK_OUT_DATE = calculateCurrentDate(20);

        public String calculateCurrentDate(int daysToAdd) {
            LocalDate today = LocalDate.now();
            LocalDate flightDate = today.plusDays(daysToAdd);
            return flightDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        @Test
        public void testCreateToken() {
            CreateTokenRequest tokenRequest = new CreateTokenRequest(TOKEN_USERNAME, TOKEN_PASSWORD);

            CreateTokenResponse tokenResponse = given()
                    .baseUri(BASE_URL)
                    .header("Content-Type", "application/json")
                    .body(tokenRequest)
                    .when()
                    .post("/auth")
                    .then()
                    .statusCode(200)
                    .body("token", notNullValue())
                    .extract()
                    .as(CreateTokenResponse.class);

            String accessToken = tokenResponse.getToken();

            Assertions.assertNotNull(accessToken, "Token should not be null");
            SharedData.saveData("accessToken", accessToken);

        }

        @Test
        public void testGetBookingIdsWithParams() throws Exception {

            Response response = given()
                    .baseUri(BASE_URL)
                    .when()
                    .get("/booking")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            ObjectMapper objectMapper = new ObjectMapper();
            List<GetBookingIdsResponse> bookingIds = objectMapper.readValue(response.asString(), new TypeReference<List<GetBookingIdsResponse>>() {
            });

            Assertions.assertFalse(bookingIds.isEmpty(), "Booking IDs list should not be empty");
            GetBookingIdsResponse randomBookingId = bookingIds.get(random.nextInt(bookingIds.size()));
            logger.info("Randomly Selected Booking ID: " + randomBookingId.getBookingid());

        }


        @Test
        public void testCreateBooking() throws Exception {
            CreateBookingRequest.BookingDates bookingDates = new CreateBookingRequest.BookingDates(CHECK_IN_DATE, CHECK_OUT_DATE);
            CreateBookingRequest bookingRequest = new CreateBookingRequest(
                    "John",
                    "Doe",
                    150,
                    true,
                    bookingDates,
                    "Breakfast"
            );

            CreateBookingResponse createBookingResponse = given()
                    .baseUri(BASE_URL)
                    .header("Content-Type", "application/json")
                    .body(bookingRequest)
                    .when()
                    .post("/booking")
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(CreateBookingResponse.class);

            Assertions.assertNotNull(createBookingResponse, "Response should not be null");
            Assertions.assertEquals("John", createBookingResponse.getBooking().getFirstname(), "Firstname should match");
            Assertions.assertEquals("Doe", createBookingResponse.getBooking().getLastname(), "Lastname should match");

            SharedData.saveData("createdBookingId", createBookingResponse.getBookingid());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createBookingResponse);
            logger.info("Response in JSON format: " + jsonResponse);

        }

        @Test
        public void testUpdateBooking() {
            testCreateToken();
            UpdateBookingRequest.BookingDates bookingDates = new UpdateBookingRequest.BookingDates(CHECK_IN_DATE, CHECK_OUT_DATE);
            UpdateBookingRequest updateBookingRequest = new UpdateBookingRequest(
                    "James",
                    "Brown",
                    111,
                    true,
                    bookingDates,
                    "Breakfast"
            );

            Response response = given()
                    .baseUri(BASE_URL)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Cookie", "token=" + SharedData.getData("accessToken"))
                    .body(updateBookingRequest)
                    .when()
                    .put("/booking/10")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            UpdateBookingResponse updateBookingResponse = response.as(UpdateBookingResponse.class);

            Assertions.assertNotNull(updateBookingResponse, "Response should not be null");
            Assertions.assertEquals("James", updateBookingResponse.getFirstname(), "Firstname should match");
            Assertions.assertEquals("Brown", updateBookingResponse.getLastname(), "Lastname should match");

            logger.info("Response: " + response.prettyPrint());
        }

        @Test
        public void testPartialUpdateBooking() {
            testCreateToken();
            PartialUpdateBookingRequest.BookingDates bookingDates = new PartialUpdateBookingRequest.BookingDates(CHECK_IN_DATE, CHECK_OUT_DATE);

            PartialUpdateBookingRequest partialUpdateRequest = new PartialUpdateBookingRequest();
            partialUpdateRequest.setFirstname("John");
            partialUpdateRequest.setLastname("White");
            partialUpdateRequest.setTotalprice(250);
            partialUpdateRequest.setBookingdates(bookingDates);
            partialUpdateRequest.setAdditionalneeds("Lunch");

            Response response = given()
                    .baseUri(BASE_URL)
                    .header("Content-Type", "application/json")
                    .header("Cookie", "token=" + SharedData.getData("accessToken"))
                    .body(partialUpdateRequest)
                    .when()
                    .patch("/booking/10")
                    .then()
                    .statusCode(200)
                    .extract().response();

            Assertions.assertNotNull(response, "Response should not be null");
            logger.info("Response: " + response.prettyPrint());
        }

        @Test
        public void testDeleteBooking() throws Exception {
            testCreateToken();
            testCreateBooking();

            Integer createdBookingId = SharedData.getData(Integer.class, "createdBookingId");

            Response response = given()
                    .baseUri(BASE_URL)
                    .header("Cookie", "token=" + SharedData.getData("accessToken"))
                    .when()
                    .delete("/booking/" + createdBookingId)
                    .then()
                    .statusCode(201)
                    .extract().response();

            Assertions.assertEquals(201, response.getStatusCode(), "Status code should indicate successful deletion.");
            logger.info("Booking with ID " + createdBookingId + " has been deleted successfully.");
        }
    }
}

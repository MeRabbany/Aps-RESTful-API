package programmer.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import programmer.restful.entity.Contact;
import programmer.restful.entity.User;
import programmer.restful.model.ContactResponse;
import programmer.restful.model.CreateContactRequest;
import programmer.restful.model.UpdateContactRequest;
import programmer.restful.model.WebResponse;
import programmer.restful.repository.ContactRespository;
import programmer.restful.repository.UserRepository;
import programmer.restful.security.BCrypt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRespository contactRespository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRespository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("Ibnu");
        user.setUsername("ibnu");
        user.setPassword(BCrypt.hashpw("ibnu",BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });

    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Ibnu");
        request.setLastName("Rabbani");
        request.setPhone("0812345678910");
        request.setEmail("mhmdibnurbbn@gmail.com");

        mockMvc.perform(
                post("/api/contacts")
                        .header("X-API-TOKEN","test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>()
            {});
            assertNull(response.getError());
            assertEquals("Ibnu",response.getData().getFirstName());
            assertEquals("Rabbani",response.getData().getLastName());
            assertEquals("0812345678910",response.getData().getPhone());
            assertEquals("mhmdibnurbbn@gmail.com",response.getData().getEmail());

            assertTrue(contactRespository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getContactNotFound() throws Exception {

        mockMvc.perform(
                get("/api/contacts/30377939370")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("ibnu").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Ibnu");
        contact.setLastName("Rabbani");
        contact.setEmail("mhmdibnurbbn@gmail.com");
        contact.setPhone("0812345678910");
        contactRespository.save(contact);

        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(contact.getId(),response.getData().getId());
            assertEquals(contact.getFirstName(),response.getData().getFirstName());
            assertEquals(contact.getLastName(),response.getData().getLastName());
            assertEquals(contact.getEmail(),response.getData().getEmail());
            assertEquals(contact.getPhone(),response.getData().getPhone());
        });
    }

    @Test
    void updateContactBadRequest() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
                put("/api/contacts/790709")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void updateContactSuccess() throws Exception {
        User user = userRepository.findById("ibnu").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Ibnu");
        contact.setLastName("Rabbani");
        contact.setEmail("mhmdibnurbbn@gmail.com");
        contact.setPhone("0812345678910");
        contactRespository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("Bani");
        request.setLastName("Robbani");
        request.setPhone("4770937093");
        request.setEmail("ibnu@gmail.com");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .header("X-API-TOKEN","test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>()
            {});
            assertNull(response.getError());
            assertEquals(request.getFirstName(),response.getData().getFirstName());
            assertEquals(request.getLastName(),response.getData().getLastName());
            assertEquals(request.getPhone(),response.getData().getPhone());
            assertEquals(request.getEmail(),response.getData().getEmail());

            assertEquals("Bani",response.getData().getFirstName());
            assertEquals("Robbani",response.getData().getLastName());

        });
    }

    @Test
    void contactFailedToDelete() throws Exception {

        mockMvc.perform(
                get("/api/contacts/30377939370")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void contactSuccessToDelete() throws Exception {
        User user = userRepository.findById("ibnu").orElseThrow();

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Ibnu");
        contact.setLastName("Rabbani");
        contact.setEmail("mhmdibnurbbn@gmail.com");
        contact.setPhone("0812345678910");
        contactRespository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("OK",response.getData());
        });
    }

    @Test
    void searchNotFound() throws Exception {

        mockMvc.perform(
                get("/api/contacts")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(0,response.getData().size());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(0,response.getPaging().getTotalPages());
            assertEquals(10,response.getPaging().getSize());
        });
    }

    @Test
    void searchSuccess() throws Exception {
        User user = userRepository.findById("ibnu").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setUser(user);
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstName("Ibnu" + i);
            contact.setLastName("Rabbani");
            contact.setEmail("mhmdibnurbbn@gmail.com");
            contact.setPhone("0812345678910");
            contactRespository.save(contact);
        }

//        USING FIRST NAME
        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Ibnu")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(10,response.getData().size());
            assertEquals(10,response.getPaging().getTotalPages());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });

//        USING LAST NAME
            mockMvc.perform(
                    get("/api/contacts")
                            .queryParam("name", "Rabbani")
                            .header("X-API-TOKEN", "test")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                    status().isOk()
            ).andDo(result -> {
                WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNull(response.getError());
                assertEquals(10,response.getData().size());
                assertEquals(10,response.getPaging().getTotalPages());
                assertEquals(0,response.getPaging().getCurrentPage());
                assertEquals(10,response.getPaging().getSize());
        });

//            USING EMAIL
        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "mhmdibnurbbn@gmail.com")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(10,response.getData().size());
            assertEquals(10,response.getPaging().getTotalPages());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });

//        USING PHONE
        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "0812345678910")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(10,response.getData().size());
            assertEquals(10,response.getPaging().getTotalPages());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Ibnu")
                        .queryParam("page","9")
                        .header("X-API-TOKEN", "test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
//            UKURAN DATA DALAM SATU HALAMAN
            assertEquals(10,response.getData().size());
//            TOTAL HALAMAN DALAM SATU PAGE
            assertEquals(10,response.getPaging().getTotalPages());
//            HALAMAN YANG INGIN DIBUKA
            assertEquals(9,response.getPaging().getCurrentPage());
//            UKURAN HALAMAN DALAM SATU PAGE
            assertEquals(10,response.getPaging().getSize());
        });
    }
}
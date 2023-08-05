package com.example.demo.security;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;
    private String bearer;


    @Test
    public void securityConfigTest(){

        ObjectMapper mapper =  new ObjectMapper();
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Superuser");
        request.setPassword("secret.password");
        request.setConfirmPassword("secret.password");

        // create User

        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user/create")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Superuser"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // login with password

        try {


         MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                            .post("/login")
                            .content("{\"username\":\"Superuser\",\"password\":\"secret.password\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                 .andReturn();
         // save jWT Token
         bearer = result.getResponse().getHeader("Authorization");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {


            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user/"+request.getUsername())
                            .header("Authorization",bearer)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // login with invalid token
        try {

            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user/"+request.getUsername())
                            .header("Authorization","invalid_token")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

}

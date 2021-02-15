package com.example.demo.controller;

import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
public class CartControllerTest {

    private static final long ITEM_ID = 1;
    private static final long NO_ITEM_ID = 10;
    private static final int QUANTITY = 2;
    private static final int NEW_QUANTITY = 1;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> createUserRequestJson;

    @Autowired
    private JacksonTester<ModifyCartRequest> modifyCartRequestJson;

    @Test
    public void addToCartSuccessful() throws Exception {
        final String username = "addToCartSuccessful_username";
        createUser(username);

        addToCart(username)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(ITEM_ID))
                .andExpect(jsonPath("$.items[1].id").value(ITEM_ID))
                .andExpect(jsonPath("$.items[2]").doesNotExist());

    }

    @Test
    public void addToCartUserNotFound() throws Exception {
       addToCart("addToCartUserNotFound_username")
               .andExpect(status().isNotFound());
    }

    @Test
    public void addToCartItemNotFound() throws Exception {
        final String username = "addToCartItemNotFound_username";
        createUser(username);

        addToCart(username, NO_ITEM_ID)
                .andExpect(status().isNotFound());
    }

    @Test
    public void removeFromCartSuccessful() throws Exception {
        final String username = "removeFromCartSuccessful_username";
        createUser(username);
        addToCart(username);

        removeFromCart(username)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(ITEM_ID))
                .andExpect(jsonPath("$.items[1]").doesNotExist());
    }

    @Test
    public void removeFromCartUserNotFound() throws Exception {
        removeFromCart("removeFromCartUserNotFound_username")
                .andExpect(status().isNotFound());
    }

    @Test
    public void removeFromCartItemNotFound() throws Exception {
        final String username = "removeFromCartItemNotFound_username";
        createUser(username);

        removeFromCart(username, NO_ITEM_ID)
                .andExpect(status().isNotFound());
    }

    private void createUser(String username) throws Exception {
        mvc.perform(post(new URI("/api/user/create"))
                .content(createUserRequestJson.write(TestUtil.createUserRequest(username, username)).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8));
    }

    private ResultActions addToCart(String username) throws Exception {
        return addToCart(username, ITEM_ID);
    }

    private ResultActions addToCart(String username, Long itemId) throws Exception {
        final ModifyCartRequest addToCartRequest = new ModifyCartRequest();
        addToCartRequest.setUsername(username);
        addToCartRequest.setItemId(itemId);
        addToCartRequest.setQuantity(QUANTITY);
        return mvc.perform(
                post(new URI("/api/cart/addToCart"))
                        .content(modifyCartRequestJson.write(addToCartRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );
    }

    private ResultActions removeFromCart(String username) throws Exception {
        return removeFromCart(username, ITEM_ID);
    }

    private ResultActions removeFromCart(String username, Long itemId) throws Exception {
        final ModifyCartRequest removeFromCartRequest = new ModifyCartRequest();
        removeFromCartRequest.setUsername(username);
        removeFromCartRequest.setItemId(itemId);
        removeFromCartRequest.setQuantity(NEW_QUANTITY);
        return mvc.perform(
                post(new URI("/api/cart/removeFromCart"))
                        .content(modifyCartRequestJson.write(removeFromCartRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
        );
    }

}

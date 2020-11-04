package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.example.demo.TestUtils.createItem;
import static com.example.demo.TestUtils.createUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository",itemRepository);
    }

    @Test
    public void add_to_cart_happy_path() {
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest cartRequest = new ModifyCartRequest("test", 1,1);
        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals("test",response.getBody().getUser().getUsername());
        assertEquals(2,response.getBody().getItems().size());

    }

    @Test
    public void remove_from_cart_happy_path(){
        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest cartRequest = new ModifyCartRequest("test", 1,1);
        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        assertEquals("test",response.getBody().getUser().getUsername());
        assertEquals(0,response.getBody().getItems().size());
    }
}

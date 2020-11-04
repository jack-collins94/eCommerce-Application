package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private Item item;

    private List<Item> items = new ArrayList<>();

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        item = new Item();
        item.setId(1L);
        item.setName("Test item");
        item.setPrice(new BigDecimal(9.99));
        item.setDescription("This is an item test description");

        items.add(item);
        items.add(item);
        items.add(item);
    }

    @Test
    public void find_by_id_happy_path(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        assertEquals(200,responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("Test item", responseEntity.getBody().getName());
    }

    @Test
    public void find_all_items_happy_path(){
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCodeValue());
        assertEquals(3,responseEntity.getBody().size());
    }

    @Test
    public void find_items_by_name_happy_path(){
        when(itemRepository.findByName("test")).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("test");
        assertEquals(200,responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity);
        assertEquals("Test item", responseEntity.getBody().get(0).getName());
        assertEquals(3,responseEntity.getBody().size());
    }
}

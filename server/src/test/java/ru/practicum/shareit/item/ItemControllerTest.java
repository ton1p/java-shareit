package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private final Item item = Item.builder()
            .id(1L)
            .available(true)
            .owner(
                    User.builder()
                            .email("test@test.com")
                            .name("Test")
                            .id(1L)
                            .build()
            )
            .name("test")
            .description("test")
            .requestId(1L)
            .build();

    @Test
    void getItems() throws Exception {
        ItemDto itemDto = ItemMapper.INSTANCE.itemToItemDto(item);
        Mockito.when(itemService.getItems(Mockito.anyLong())).thenReturn(List.of(itemDto));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/items")
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].ownerId").value(itemDto.getOwnerId()));
    }

    @Test
    void getItem() throws Exception {
        ItemOwnerDto itemOwnerDto = ItemMapper.INSTANCE.itemToItemOwnerDto(item);
        Mockito.when(itemService.getItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemOwnerDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/items/{id}", item.getId())
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$.id").value(itemOwnerDto.getId()))
                .andExpect(jsonPath("$.available").value(itemOwnerDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemOwnerDto.getDescription()))
                .andExpect(jsonPath("$.name").value(itemOwnerDto.getName()))
                .andExpect(jsonPath("$.ownerId").value(itemOwnerDto.getOwnerId()))
                .andExpect(jsonPath("$.lastBooking").value(itemOwnerDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemOwnerDto.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemOwnerDto.getComments()));
    }

    @Test
    void search() throws Exception {
        ItemDto itemDto = ItemMapper.INSTANCE.itemToItemDto(item);
        Mockito.when(itemService.search(Mockito.anyString())).thenReturn(List.of(itemDto));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/items/search?text={text}", "test")
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].ownerId").value(itemDto.getOwnerId()));
    }

    @Test
    void createItem() throws Exception {
        ItemDto itemDto = ItemMapper.INSTANCE.itemToItemDto(item);
        Mockito.when(itemService.createItem(Mockito.anyLong(), Mockito.any())).thenReturn(itemDto);
        CreateItemDto createItemDto = new CreateItemDto("test", "test", true, 1L);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/items")
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(createItemDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.ownerId").value(itemDto.getOwnerId()));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto itemDto = ItemMapper.INSTANCE.itemToItemDto(item);
        Mockito.when(itemService.updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(itemDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/items/{itemId}", 1)
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(itemDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.ownerId").value(itemDto.getOwnerId()));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "test", "test", LocalDateTime.now());
        Mockito.when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(commentDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(commentDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));
    }
}

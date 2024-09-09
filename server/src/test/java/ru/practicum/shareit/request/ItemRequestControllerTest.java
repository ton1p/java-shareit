package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "test", LocalDateTime.now(), new ArrayList<>());

        Mockito.when(itemRequestService.createRequest(Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemRequestDto);

        CreateItemRequestDto createItemRequestDto = CreateItemRequestDto.builder().description("test").build();
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/requests")
                                .content(objectMapper.writeValueAsString(createItemRequestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.items").value(itemRequestDto.getItems()));
    }

    @Test
    void getOwnRequests() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "test", LocalDateTime.now(), new ArrayList<>());

        Mockito.when(itemRequestService.getOwnRequests(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/requests")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].items").value(itemRequestDto.getItems()));
    }

    @Test
    void getAllRequests() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "test", LocalDateTime.now(), new ArrayList<>());

        Mockito.when(itemRequestService.getAllRequests())
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/requests/all")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].items").value(itemRequestDto.getItems()));
    }

    @Test
    void getRequestById() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "test", LocalDateTime.now(), new ArrayList<>());

        Mockito.when(itemRequestService.getRequestById(Mockito.anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/requests/{requestId}", itemRequestDto.getId())
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.items").value(itemRequestDto.getItems()));
    }
}

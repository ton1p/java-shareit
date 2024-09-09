package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private static final String ITEM_NOT_FOUND_ERROR_MESSAGE = "Item not found";
    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(String query) {
        return itemRepository.search(query).stream().map(ItemMapper.INSTANCE::itemToItemDto).toList();
    }

    @Override
    public ItemOwnerDto getItem(Long userId, Long id) {
        LocalDateTime now = LocalDateTime.now();
        ItemOwnerDto item = itemRepository.findById(id)
                .map(ItemMapper.INSTANCE::itemToItemOwnerDto)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_ERROR_MESSAGE));

        if (item.getOwnerId().equals(userId)) {
            ShortBookingDto lastBooking = bookingRepository
                    .findFirstByItemIdAndStatusAndStartIsBeforeOrStartEquals(
                            item.getId(),
                            BookingStatus.APPROVED,
                            now,
                            now
                    ).map(BookingMapper.INSTANCE::bookingToShortBookingDto).orElse(null);

            ShortBookingDto nextBooking = bookingRepository
                    .findFirstByItemIdAndStatusAndStartIsAfterOrStartEquals(
                            item.getId(),
                            BookingStatus.APPROVED,
                            now,
                            now
                    ).map(BookingMapper.INSTANCE::bookingToShortBookingDto).orElse(null);

            item.setLastBooking(lastBooking);
            item.setNextBooking(nextBooking);
        }

        List<CommentDto> comments = commentRepository.findAllByItemId(item.getId())
                .stream()
                .map(CommentMapper.INSTANCE::commentToCommentDto)
                .toList();

        item.setComments(comments);

        return item;
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, CreateItemDto createItemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_ERROR_MESSAGE);
        }
        Item newItem = ItemMapper.INSTANCE.createItemDtoToItem(createItemDto);
        newItem.setOwner(user.get());
        Item saved = itemRepository.save(newItem);
        return ItemMapper.INSTANCE.itemToItemDto(saved);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_ERROR_MESSAGE);
        }

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw new NotFoundException(ITEM_NOT_FOUND_ERROR_MESSAGE);
        }
        if (item.getName() != null && !item.getName().trim().isEmpty()) {
            item.setName(itemDto.getName());
        }
        if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
            item.setDescription(itemDto.getDescription());
        }
        if (item.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.INSTANCE.itemToItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CreateCommentDto createCommentDto) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_ERROR_MESSAGE));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_ERROR_MESSAGE));
        List<Booking> userBookings = bookingRepository.findAllByBookerIdAndItemId(userId, itemId);

        if (userBookings.isEmpty()) {
            throw new BadRequestException("Вы не брали эту вещь в аренду");
        }

        List<Booking> completedBookings = userBookings
                .stream()
                .filter(booking -> booking.getEnd().isBefore(now))
                .toList();

        if (completedBookings.isEmpty()) {
            throw new BadRequestException("Вы еще не завершили бронирование этой вещи и не можете оставить отзыв");
        }

        Comment comment = new Comment();
        comment.setText(createCommentDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(now);

        return CommentMapper.INSTANCE.commentToCommentDto(commentRepository.save(comment));
    }
}

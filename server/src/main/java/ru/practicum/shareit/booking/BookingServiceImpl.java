package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.GetBookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> getAllByBookerIdAndState(Long userId, GetBookingState state) {
        LocalDateTime now = LocalDateTime.now();
        List<BookingDto> result;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL -> result = mapListToBookingDto(bookingRepository.findAllByBookerId(userId, sort));
            case CURRENT ->
                    result = mapListToBookingDto(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(userId, now, now, sort));
            case PAST ->
                    result = mapListToBookingDto(bookingRepository.findAllByBookerIdAndEndIsBefore(userId, now, sort));
            case FUTURE ->
                    result = mapListToBookingDto(bookingRepository.findAllByBookerIdAndStartIsAfter(userId, now, sort));
            case WAITING ->
                    result = mapListToBookingDto(bookingRepository.findAllByBookerIdAndStatusIs(userId, BookingStatus.WAITING, sort));
            case REJECTED ->
                    result = mapListToBookingDto(bookingRepository.findAllByBookerIdAndStatusIs(userId, BookingStatus.REJECTED, sort));
            default -> throw new BadRequestException("Неверный тип параметра");
        }
        return result;
    }

    @Override
    public List<BookingDto> getAllByOwnerIdAndState(Long userId, GetBookingState state) {
        LocalDateTime now = LocalDateTime.now();
        List<BookingDto> result;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL -> result = mapListToBookingDto(bookingRepository.findAllByItemOwnerId(userId, sort));
            case CURRENT ->
                    result = mapListToBookingDto(bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, now, now, sort));
            case PAST ->
                    result = mapListToBookingDto(bookingRepository.findAllByItemOwnerIdAndEndIsBefore(userId, now, sort));
            case FUTURE ->
                    result = mapListToBookingDto(bookingRepository.findAllByItemOwnerIdAndStartIsAfter(userId, now, sort));
            case WAITING ->
                    result = mapListToBookingDto(bookingRepository.findAllByItemOwnerIdAndStatusIs(userId, BookingStatus.WAITING, sort));
            case REJECTED ->
                    result = mapListToBookingDto(bookingRepository.findAllByItemOwnerIdAndStatusIs(userId, BookingStatus.REJECTED, sort));
            default -> throw new BadRequestException("Неверный тип параметра");
        }
        if (result.isEmpty()) {
            throw new NotFoundException("У вас нет ни одной вещи");
        }
        return result;
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Optional<Booking> booking = bookingRepository.findByIdAndBookerIdOrItemOwnerId(bookingId, userId, userId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирование не найдено или вы не можете просматривать данное бронирование");
        }
        return BookingMapper.INSTANCE.bookingToBookingDto(booking.get());
    }

    @Override
    @Transactional
    public BookingDto create(Long userId, CreateBookingDto createBookingDto) {
        UserDto userDto = userService.findById(userId);
        Optional<Item> item = itemRepository.findById(createBookingDto.getItemId());

        if (item.isEmpty()) {
            throw new NotFoundException("Item not found");
        }

        Item itemFound = item.get();

        if (Boolean.FALSE.equals(itemFound.getAvailable())) {
            throw new BadRequestException("В данный момент эта вешь не доступна для бронирования");
        }

        Booking booking = BookingMapper.INSTANCE.createBookingDtoToBooking(createBookingDto);

        booking.setBooker(UserMapper.INSTANCE.userDtoToUser(userDto));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(itemFound);

        return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Booking not found");
        }
        Booking bookingForUpdate = booking.get();
        if (!Objects.equals(bookingForUpdate.getItem().getOwner().getId(), userId)) {
            throw new BadRequestException("Вы не можете менять статус бронирования");
        }
        bookingForUpdate.setStatus(Boolean.TRUE.equals(approved) ? BookingStatus.APPROVED : BookingStatus.WAITING);
        return BookingMapper.INSTANCE.bookingToBookingDto(bookingRepository.save(bookingForUpdate));
    }

    private List<BookingDto> mapListToBookingDto(List<Booking> list) {
        return list.stream().map(BookingMapper.INSTANCE::bookingToBookingDto).toList();
    }
}

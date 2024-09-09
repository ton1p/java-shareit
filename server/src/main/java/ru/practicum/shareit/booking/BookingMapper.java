package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking createBookingDtoToBooking(CreateBookingDto createBookingDto);

    @Mapping(source = "item.owner.id", target = "item.ownerId")
    BookingDto bookingToBookingDto(Booking booking);

    ShortBookingDto bookingToShortBookingDto(Booking booking);
}

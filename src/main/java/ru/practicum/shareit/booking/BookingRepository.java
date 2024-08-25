package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdAndBookerIdOrItemOwnerId(Long id, Long bookerId, Long itemOwnerId);

    List<Booking> findAllByBookerId(Long userId, Sort sort);

    List<Booking> findAllByBookerIdAndItemId(Long userId, Long itemId);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(Long userId, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(Long userId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatusIs(Long userId, BookingStatus status, Sort sort);

    List<Booking> findAllByItemOwnerId(Long userId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long userId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(Long userId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatusIs(Long userId, BookingStatus status, Sort sort);

    Optional<Booking> findFirstByItemIdAndStatusAndStartIsBeforeOrStartEquals(Long itemId, BookingStatus status, LocalDateTime start, LocalDateTime end);

    Optional<Booking> findFirstByItemIdAndStatusAndStartIsAfterOrStartEquals(Long itemId, BookingStatus status, LocalDateTime start, LocalDateTime end);
}

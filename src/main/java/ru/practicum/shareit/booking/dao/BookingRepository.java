package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Бронирования пользователя
    List<Booking> findByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findByBooker_IdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND ?2 BETWEEN b.start AND b.end")
    List<Booking> findByBooker_IdAndCurrentTime(Long bookerId, LocalDateTime currentTime, Sort sort);

    // Бронирования владельца вещей
    List<Booking> findByItem_Owner_Id(Long ownerId, Sort sort);

    List<Booking> findByItem_Owner_IdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    List<Booking> findByItem_Owner_IdAndEndIsBefore(Long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItem_Owner_IdAndStartIsAfter(Long ownerId, LocalDateTime start, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND ?2 BETWEEN b.start AND b.end")
    List<Booking> findByItem_Owner_IdAndCurrentTime(Long ownerId, LocalDateTime currentTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.status = 'APPROVED' AND b.end < ?2")
    List<Booking> findLastBooking(Long itemId, LocalDateTime currentTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.status = 'APPROVED' AND b.start > ?2")
    List<Booking> findNextBooking(Long itemId, LocalDateTime currentTime, Sort sort);

    // Проверка на пересечение дат
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.item.id = ?1 AND b.status != 'REJECTED' AND " +
            "((b.start <= ?2 AND b.end > ?2) OR (b.start < ?3 AND b.end >= ?3) OR (b.start >= ?2 AND b.end <= ?3))")
    boolean existsOverlappingBooking(Long itemId, LocalDateTime start, LocalDateTime end);

    // Найти бронирования по списку ID вещей и статусу, отсортированные по дате начала
    List<Booking> findByItemIdInAndStatusOrderByStartAsc(List<Long> itemIds, BookingStatus status);

    // Проверить существование завершенного бронирования для пользователя и вещи
    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(Long bookerId, Long itemId, BookingStatus status, LocalDateTime end);
}

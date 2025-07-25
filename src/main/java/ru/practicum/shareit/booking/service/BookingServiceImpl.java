package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart()) ||
                bookingRequestDto.getEnd().equals(bookingRequestDto.getStart())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + bookingRequestDto.getItemId() + " не найдена"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Владелец не может забронировать свою вещь");
        }

        if (bookingRepository.existsOverlappingBooking(item.getId(),
                bookingRequestDto.getStart(), bookingRequestDto.getEnd())) {
            throw new ValidationException("Вещь уже забронирована на указанные даты");
        }

        Booking booking = bookingMapper.toBooking(bookingRequestDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с ID " + bookingId + " не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Только владелец вещи может подтвердить или отклонить бронирование");
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Можно изменить статус только у бронирования в статусе WAITING");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с ID " + bookingId + " не найдено"));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Доступ к бронированию разрешен только автору или владельцу вещи");
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестный статус: " + state);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findByBooker_Id(userId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndCurrentTime(userId, now, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndEndIsBefore(userId, now, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBooker_IdAndStartIsAfter(userId, now, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED, sort);
                break;
            default:
                throw new ValidationException("Неизвестный статус: " + state);
        }

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестный статус: " + state);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findByItem_Owner_Id(userId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItem_Owner_IdAndCurrentTime(userId, now, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByItem_Owner_IdAndEndIsBefore(userId, now, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfter(userId, now, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(userId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(userId, BookingStatus.REJECTED, sort);
                break;
            default:
                throw new ValidationException("Неизвестный статус: " + state);
        }

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}

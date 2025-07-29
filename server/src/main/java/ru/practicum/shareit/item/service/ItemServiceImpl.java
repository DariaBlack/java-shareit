package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с ID " + itemDto.getRequestId() + " не найден"));
            item.setRequest(request);
        }

        Item savedItem = itemRepository.save(item);
        return itemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не является владельцем вещи с ID " + itemId);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(item);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemWithBookingDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        ItemWithBookingDto itemDto = itemMapper.toItemWithBookingDto(item);

        // Добавляем комментарии
        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId);
        itemDto.setComments(comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));

        // Добавляем информацию о бронированиях только для владельца
        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            Sort sortDesc = Sort.by(Sort.Direction.DESC, "start");
            Sort sortAsc = Sort.by(Sort.Direction.ASC, "start");

            // Последнее завершенное или текущее бронирование
            List<Booking> lastBookings = bookingRepository.findLastBooking(itemId, now, sortDesc);
            BookingShortDto lastBooking = lastBookings.isEmpty() ? null :
                    new BookingShortDto(lastBookings.get(0).getId(), lastBookings.get(0).getBooker().getId(),
                            lastBookings.get(0).getStart(), lastBookings.get(0).getEnd());

            // Следующее будущее бронирование
            List<Booking> nextBookings = bookingRepository.findNextBooking(itemId, now, sortAsc);
            BookingShortDto nextBooking = nextBookings.isEmpty() ? null :
                    new BookingShortDto(nextBookings.get(0).getId(), nextBookings.get(0).getBooker().getId(),
                            nextBookings.get(0).getStart(), nextBookings.get(0).getEnd());

            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
        }

        return itemDto;
    }

    @Override
    public List<ItemWithBookingDto> getItemsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        List<Item> items = itemRepository.findByOwnerIdOrderByIdAsc(userId);

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        // Получаем все бронирования для вещей пользователя
        List<Booking> bookings = bookingRepository.findByItemIdInAndStatusOrderByStartAsc(
                itemIds, BookingStatus.APPROVED);

        // Получаем все комментарии для вещей пользователя
        List<Comment> comments = commentRepository.findByItemIdInOrderByCreatedDesc(itemIds);

        // Группируем бронирования по ID вещи
        Map<Long, List<Booking>> bookingsByItemId = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        // Группируем комментарии по ID вещи
        Map<Long, List<CommentDto>> commentsByItemId = comments.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(commentMapper::toCommentDto, Collectors.toList())
                ));

        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItemId.getOrDefault(item.getId(), Collections.emptyList());

                    BookingShortDto lastBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isBefore(now))
                            .max((b1, b2) -> b1.getStart().compareTo(b2.getStart()))
                            .map(booking -> new BookingShortDto(booking.getId(), booking.getBooker().getId(),
                                    booking.getStart(), booking.getEnd()))
                            .orElse(null);

                    BookingShortDto nextBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().isAfter(now))
                            .min((b1, b2) -> b1.getStart().compareTo(b2.getStart()))
                            .map(booking -> new BookingShortDto(booking.getId(), booking.getBooker().getId(),
                                    booking.getStart(), booking.getEnd()))
                            .orElse(null);

                    List<CommentDto> itemComments = commentsByItemId.getOrDefault(item.getId(), Collections.emptyList());

                    return new ItemWithBookingDto(
                            item.getId(),
                            item.getName(),
                            item.getDescription(),
                            item.getAvailable(),
                            item.getRequest() != null ? item.getRequest().getId() : null,
                            lastBooking,
                            nextBooking,
                            itemComments
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не является владельцем вещи с ID " + itemId);
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        // Проверяем только то, что бронирование завершилось (без учета статуса)
        LocalDateTime now = LocalDateTime.now();
        boolean hasCompletedBooking = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, now);

        if (!hasCompletedBooking) {
            throw new BadRequestException("Пользователь с id " + userId + " не брал вещь с itemId " + itemId + " в аренду");
        }

        Comment comment = commentMapper.toComment(commentRequestDto, item, author);
        comment.setCreated(now);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toCommentDto(savedComment);
    }

}

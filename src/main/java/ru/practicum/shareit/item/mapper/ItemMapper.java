package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "requestId", source = "request.id")
    @Mapping(target = "comments", ignore = true)
    ItemDto toItemDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "request", ignore = true)
    Item toItem(ItemDto itemDto);

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", expression = "java(java.util.Collections.emptyList())")
    ItemWithBookingDto toItemWithBookingDto(Item item);
}

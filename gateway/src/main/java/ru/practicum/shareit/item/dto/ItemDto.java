package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название вещи не может быть пустым")
    @Size(max = 255, message = "Название не может превышать 255 символов")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым")
    @Size(max = 512, message = "Описание не может превышать 512 символов")
    private String description;

    @NotNull(message = "Статус доступности вещи должен быть указан")
    private Boolean available;

    private Long requestId;
}

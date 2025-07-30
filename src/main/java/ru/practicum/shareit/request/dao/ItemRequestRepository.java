package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long requestorId);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requestor.id != :userId ORDER BY ir.created DESC")
    List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(@Param("userId") Long userId, Pageable pageable);
}

package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.item.id IN :itemIds ORDER BY c.created DESC")
    List<Comment> findByItemIdInOrderByCreatedDesc(@Param("itemIds") List<Long> itemIds);

    @Query("SELECT c FROM Comment c WHERE c.item.owner.id = :ownerId ORDER BY c.created DESC")
    List<Comment> findByItemOwnerIdOrderByCreatedDesc(@Param("ownerId") Long ownerId);

    @Query("SELECT c FROM Comment c WHERE c.item.id = :itemId ORDER BY c.created DESC")
    List<Comment> findByItemIdOrderByCreatedDesc(@Param("itemId") Long itemId);
}

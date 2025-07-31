package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void testCommentConstructorAndGetters() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment(1L, "Great item!", item, author, created);

        assertEquals(1L, comment.getId());
        assertEquals("Great item!", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void testSetters() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment();

        comment.setId(2L);
        comment.setText("Updated comment");
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(created);

        assertEquals(2L, comment.getId());
        assertEquals("Updated comment", comment.getText());
        assertEquals(item, comment.getItem());
        assertEquals(author, comment.getAuthor());
        assertEquals(created, comment.getCreated());
    }

    @Test
    void testEquals() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment1 = new Comment(1L, "Great item!", item, author, created);
        Comment comment2 = new Comment(1L, "Great item!", item, author, created);
        Comment comment3 = new Comment(2L, "Another comment", item, author, created);

        assertEquals(comment1, comment2);
        assertNotEquals(comment1, comment3);
    }

    @Test
    void testHashCode() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment1 = new Comment(1L, "Great item!", item, author, created);
        Comment comment2 = new Comment(1L, "Great item!", item, author, created);

        assertEquals(comment1.hashCode(), comment2.hashCode());
    }

    @Test
    void testToString() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment(1L, "Great item!", item, author, created);

        String actualToString = comment.toString();
        assertTrue(actualToString.contains("Comment("));
        assertTrue(actualToString.contains("id=1"));
        assertTrue(actualToString.contains("text=Great item!"));
    }

    @Test
    void testEqualsWithNull() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment(1L, "Great item!", item, author, created);

        assertNotEquals(comment, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        User author = new User(1L, "Author", "author@example.com");
        Item item = new Item();
        LocalDateTime created = LocalDateTime.now();
        Comment comment = new Comment(1L, "Great item!", item, author, created);

        assertNotEquals(comment, new Object());
    }
}

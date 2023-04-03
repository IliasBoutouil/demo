package me.ilias.repositories;

import me.ilias.domains.Author;
import me.ilias.domains.Book;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository underTest;

    @Autowired
     AuthorRepository authorRepository;
    Book book;

    @BeforeEach
    void setUp()
    {
        authorRepository.deleteAll();
        Author author = authorRepository.save(Author.builder().id(1L).fullName("Jon Doe").build());
        book = Book.builder()
                .title("handbook")
                .pages(10)
                .summary("lorem summary for the book")
                .publishDate(LocalDate.of(2000,1,22))
                .author(author)
                .build();
        underTest.save(book);
    }
    @AfterEach
    void cleanUp()
    {
        underTest.deleteAll();
    }
    @Test
    void findByTitleContainsIgnoreCase() {
        //given
        String title="hand";
        //when
        Page<Book> books = underTest.findByTitleContainsIgnoreCase(title, Pageable.unpaged());
        //then
       assertTrue(books.hasContent());
       assertEquals(books.getContent().get(0).getTitle(),book.getTitle());
    }

    @Test
    void findByAuthorFullNameContainsIgnoreCase() {
        String authorName="Doe";
        Page<Book> books = underTest.findByAuthorFullNameContainsIgnoreCase(authorName, Pageable.unpaged());
        assertTrue(books.hasContent());
        assertEquals(books.getContent().get(0).getTitle(),book.getTitle());
        assertEquals(books.getContent().get(0).getAuthor().getFullName(),book.getAuthor().getFullName());

    }
}
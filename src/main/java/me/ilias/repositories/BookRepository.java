package me.ilias.repositories;

import me.ilias.domains.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book,Long> {

    Page<Book> findByTitleContainsIgnoreCase(String bookTitle, Pageable pageable);
    Page<Book> findByAuthorFullNameContainsIgnoreCase(String bookTitle, Pageable pageable);

}

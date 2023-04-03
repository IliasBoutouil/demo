package me.ilias.services;

import me.ilias.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    Page<BookDTO> getBooksPage(Pageable pageable);
    BookDTO getBookById(Long id);
    Page<BookDTO> getBooksByTitle(String bookTitle,Pageable pageable);
    Page<BookDTO> getBooksByAuthorName(String authorName,Pageable pageable);
    BookDTO storeBook(BookDTO book);
    BookDTO updateBook(BookDTO book);
    void deleteBook(Long bookId);
    List<BookDTO> uploadBooksFromCsv(MultipartFile file);
    BookDTO uploadBookCover(MultipartFile cover,Long bookId) ;
    String getBookCover(Long bookId);
}

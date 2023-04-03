package me.ilias.ressources;

import me.ilias.dto.BookDTO;
import me.ilias.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    Page<BookDTO> getBooksPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.getBooksPage(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    BookDTO getBookById(@PathVariable(name = "id") Long bookId) {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/title")
    Page<BookDTO> getBooksByTitle(@RequestParam(name = "name") String bookTitle, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.getBooksByTitle(bookTitle, PageRequest.of(page, size));
    }

    @GetMapping("/author")
    Page<BookDTO> getBooksByAuthor(@RequestParam(name = "name") String authorName, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.getBooksByAuthorName(authorName, PageRequest.of(page, size));
    }

    @PostMapping
    BookDTO saveBook(@Valid @RequestBody BookDTO book) {
        return bookService.storeBook(book);
    }

    @PutMapping
    BookDTO updateBook(@RequestBody BookDTO book) {
        return bookService.updateBook(book);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteBook(@PathVariable(name = "id") Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("deleted successfully");
    }

    @PostMapping("/upload-csv")
    List<BookDTO> uploadBooksCsvFile(@RequestParam("file") MultipartFile file) {
        return bookService.uploadBooksFromCsv(file);
    }

    @PostMapping("/{bookId}/cover")
    BookDTO uploadBooksCover(@RequestParam("cover") MultipartFile cover, @PathVariable("bookId") Long bookId) {
        return bookService.uploadBookCover(cover, bookId);
    }
    @GetMapping("/{bookId}/cover")
    String getBooksCover(@PathVariable("bookId") Long bookId) {
        return bookService.getBookCover(bookId);
    }
}

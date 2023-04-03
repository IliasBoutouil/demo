package me.ilias.services.implementations;

import lombok.SneakyThrows;
import me.ilias.domains.Book;
import me.ilias.dto.BookDTO;
import me.ilias.exceptions.NoEntityFoundException;
import me.ilias.mappers.BookMapper;
import me.ilias.repositories.BookRepository;
import me.ilias.services.BookService;
import me.ilias.utils.BooksUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImp implements BookService {

    private static final String NO_BOOK_FOUND_WITH_THE_ID = "no book found with the id ";
    //todo : add validation
    private final BookRepository bookRepository;
    private final BookMapper mapper = Mappers.getMapper(BookMapper.class);

    public BookServiceImp(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDTO uploadBookCover(MultipartFile cover, Long bookId) {
        Book book = findBookById(bookId);
        try {
            book.setCover( cover.getBytes());
        }
        catch (IOException e)
        {
            throw  new IllegalArgumentException(e.getMessage());
        }
        return convertToDto(bookRepository.save(book));
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new NoEntityFoundException(NO_BOOK_FOUND_WITH_THE_ID + bookId));
    }

    @Override
    @SneakyThrows
    public List<BookDTO> uploadBooksFromCsv(MultipartFile file) {
        List<Book> books = BooksUtil.readBooksCsv(file);
        books = bookRepository.saveAll(books);
        return books.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public String getBookCover(Long bookId) {
        Book book = findBookById(bookId);
        Base64.Encoder decoder = Base64.getEncoder();
        return decoder.encodeToString(book.getCover());
    }

    @Override
    public Page<BookDTO> getBooksPage(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return convertToPageDto(booksPage);
    }

    private BookDTO convertToDto(Book book) {
        return mapper.entityToDTO(book);
    }

    private Book convertToEntity(BookDTO bookDTO) {
        return mapper.dtoToEntity(bookDTO);
    }

    private Page<BookDTO> convertToPageDto(Page<Book> books) {
        List<BookDTO> bookDTOS = books.stream().map(this::convertToDto).collect(Collectors.toList());
        return new PageImpl<>(bookDTOS);
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = findBookById(id);
        return convertToDto(book);
    }

    @Override
    public Page<BookDTO> getBooksByTitle(String bookTitle, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findByTitleContainsIgnoreCase(bookTitle, pageable);
        return convertToPageDto(booksPage);
    }

    @Override
    public Page<BookDTO> getBooksByAuthorName(String authorName, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findByAuthorFullNameContainsIgnoreCase(authorName, pageable);
        return convertToPageDto(booksPage);
    }

    @Override
    public BookDTO storeBook(BookDTO bookDTO) {
        //todo : update this code
        try {
            Book savedBook = bookRepository.save(convertToEntity(bookDTO));
            return convertToDto(savedBook);
        } catch (Exception e) {
            throw new IllegalArgumentException("unable to save the book");
        }
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {

        //todo : update this (old values lost)
        return this.storeBook(bookDTO);
    }

    @Override
    public void deleteBook(Long bookId) {
        try {
            bookRepository.deleteById(bookId);
        } catch (Exception e) {
            throw new IllegalArgumentException("unable to delete the book");
        }
    }
}

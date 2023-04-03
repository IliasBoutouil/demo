package me.ilias.utils;

import me.ilias.domains.Author;
import me.ilias.domains.Book;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BooksUtil {

    private static Book extractBookFromCsv(CSVRecord line )
    {
        Book.BookBuilder bookBuilder = Book.builder();
        bookBuilder.title(line.get("title"));
        bookBuilder.summary(line.get("summary"));
        bookBuilder.publishDate(LocalDate.parse(line.get("publish_date")));
        bookBuilder.pages(Integer.parseInt(line.get("pages")));
        bookBuilder.author(Author.builder().id(Long.parseLong(line.get("author_id"))).build());
        return bookBuilder.build();
    }
    public static List<Book> readBooksCsv(MultipartFile file)
    {
        List<Book> books = new ArrayList<>();
        try(BufferedReader buffer = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)))
        {
            CSVParser csvParser = new CSVParser(buffer, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            for (CSVRecord line : csvParser)
            {
                books.add(extractBookFromCsv(line));
            }
        }
        catch (RuntimeException | IOException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }

        return books;
    }

}

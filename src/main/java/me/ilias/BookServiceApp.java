package me.ilias;

import me.ilias.domains.Author;
import me.ilias.domains.Book;
import me.ilias.repositories.AuthorRepository;
import me.ilias.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class BookServiceApp {
    /*todo :
        X spring security,keycloak / Ldap? ,
          http communication,
          adapter, factory patterns
          web Service? TVA
       V  file upload and manipulation
          PDF generation
          unit & integration tests,sonarQube
          log
          ObjectMapper docs
          */
    public static void main(String[] args) {
        SpringApplication.run(BookServiceApp.class, args);
    }

    @Bean
    CommandLineRunner runner(BookRepository bookRepository, AuthorRepository authorRepository) {
        return args ->
        {
            //todo : make a script instead of this
            Author author1 = authorRepository.save(Author.builder().fullName("Ilias Boutouil").build());
            Author author2 = authorRepository.save(Author.builder().fullName("Jhon Doe").build());
            for (int i = 1; i <= 10; i++) {
                bookRepository.save(Book.builder().author(i % 2 == 0 ? author2 : author1).pages(i * 50).title("Book B" + i).summary("Lorem ipsum dolor sit, amet consectetur adipisicing elit. Ut molestias vel magni adipisci laboriosam rerum possimus sit, ratione magnam, exercitationem labore, eum ex tempora quis neque. Nam doloremque saepe optio.").build());
            }
        };
    }

}

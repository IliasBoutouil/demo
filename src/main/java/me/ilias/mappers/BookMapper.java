package me.ilias.mappers;

import me.ilias.domains.Book;
import me.ilias.dto.BookDTO;
import org.mapstruct.Mapper;

import javax.validation.Valid;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO entityToDTO(Book entity);
    Book dtoToEntity(@Valid BookDTO dto);
}

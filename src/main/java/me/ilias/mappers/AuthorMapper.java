package me.ilias.mappers;

import me.ilias.domains.Author;
import me.ilias.dto.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDTO entityToDto(Author entity);
    Author dtoToEntity(AuthorDTO dto);
}

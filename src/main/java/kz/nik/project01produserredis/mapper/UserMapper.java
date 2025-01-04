package kz.nik.project01produserredis.mapper;

import kz.nik.project01produserredis.dto.UserDto;
import kz.nik.project01produserredis.model.User;
import org.mapstruct.Mapper;



import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    List<UserDto> toDtoList(List<User> list);
}
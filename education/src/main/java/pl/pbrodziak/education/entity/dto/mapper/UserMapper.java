package pl.pbrodziak.education.entity.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.pbrodziak.education.entity.User;
import pl.pbrodziak.education.entity.dto.UserDto;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "userRole", target = "userRole")
    UserDto userToUserDto(User user);

    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "userRole", target = "userRole")
    User userDtoToUser(UserDto userDto);


}

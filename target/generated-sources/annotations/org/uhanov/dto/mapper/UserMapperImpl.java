package org.uhanov.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.dto.UserFullDTO;
import org.uhanov.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T10:59:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User authToModel(UserAuthDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        if ( dto.getBalance() != null ) {
            user.balance( dto.getBalance() );
        }
        else {
            user.balance( (double) 0.0 );
        }
        user.id( dto.getId() );
        user.password( dto.getPassword() );
        user.firstname( dto.getFirstname() );
        user.surname( dto.getSurname() );
        user.nickname( dto.getNickname() );
        user.birthDate( dto.getBirthDate() );
        user.registrationDate( dto.getRegistrationDate() );
        user.country( dto.getCountry() );

        return user.build();
    }

    @Override
    public UserFullDTO toFullDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserFullDTO.UserFullDTOBuilder userFullDTO = UserFullDTO.builder();

        userFullDTO.id( user.getId() );
        userFullDTO.balance( user.getBalance() );
        userFullDTO.firstname( user.getFirstname() );
        userFullDTO.surname( user.getSurname() );
        userFullDTO.nickname( user.getNickname() );
        userFullDTO.birthDate( user.getBirthDate() );
        userFullDTO.registrationDate( user.getRegistrationDate() );
        userFullDTO.country( user.getCountry() );

        return userFullDTO.build();
    }

    @Override
    public UserAuthDTO toAuthDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserAuthDTO.UserAuthDTOBuilder userAuthDTO = UserAuthDTO.builder();

        userAuthDTO.id( user.getId() );
        userAuthDTO.password( user.getPassword() );
        userAuthDTO.balance( user.getBalance() );
        userAuthDTO.firstname( user.getFirstname() );
        userAuthDTO.surname( user.getSurname() );
        userAuthDTO.nickname( user.getNickname() );
        userAuthDTO.birthDate( user.getBirthDate() );
        userAuthDTO.registrationDate( user.getRegistrationDate() );
        userAuthDTO.country( user.getCountry() );

        return userAuthDTO.build();
    }

    @Override
    public void updateUser(UserAuthDTO dto, User entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getPassword() != null ) {
            entity.setPassword( dto.getPassword() );
        }
        if ( dto.getFirstname() != null ) {
            entity.setFirstname( dto.getFirstname() );
        }
        if ( dto.getBalance() != null ) {
            entity.setBalance( dto.getBalance() );
        }
        if ( dto.getSurname() != null ) {
            entity.setSurname( dto.getSurname() );
        }
        if ( dto.getNickname() != null ) {
            entity.setNickname( dto.getNickname() );
        }
        if ( dto.getBirthDate() != null ) {
            entity.setBirthDate( dto.getBirthDate() );
        }
        if ( dto.getRegistrationDate() != null ) {
            entity.setRegistrationDate( dto.getRegistrationDate() );
        }
        if ( dto.getCountry() != null ) {
            entity.setCountry( dto.getCountry() );
        }
    }
}

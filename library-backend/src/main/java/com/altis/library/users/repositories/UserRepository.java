package com.altis.library.users.repositories;


import com.altis.library.users.models.dtos.UserResponseDTO;
import com.altis.library.users.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}

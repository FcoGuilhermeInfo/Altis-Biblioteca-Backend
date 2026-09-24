package com.altis.library.users.repositories;


import com.altis.library.users.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndCpf(String email, String cpf);

    Page<UserEntity> findByAdminFalse(Pageable pageable);

    Page<UserEntity> findByAdminFalseAndNameContainingIgnoreCase(String name, Pageable pageable);
}

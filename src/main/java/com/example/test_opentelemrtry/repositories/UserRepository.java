package com.example.test_opentelemrtry.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.test_opentelemrtry.models.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long> {
    // This interface will automatically have methods for CRUD operations
    // due to the JpaRepository inheritance.
    // You can define custom query methods here if needed.
    @Query("SELECT u FROM User u JOIN FETCH u.profile")
    List<User> findAllWithProfile();

}

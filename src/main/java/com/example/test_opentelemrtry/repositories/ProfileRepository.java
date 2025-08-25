package com.example.test_opentelemrtry.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.test_opentelemrtry.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}

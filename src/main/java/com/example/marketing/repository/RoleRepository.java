package com.example.marketing.repository;

import com.example.marketing.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
     Role findAllByUserId(long userId);
}

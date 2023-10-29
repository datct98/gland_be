package com.example.marketing.repository;

import com.example.marketing.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);

    User findUserById(Long id);

    // Khi user chưa được add vào phòng ban nào cả
    @Query("select new User(u.id, u.username, u.fullName, u.phone, u.email, u.status, d.name) " +
            "from User u left join Department  d on u.departmentId = d.id " +
            "where d.storeId = :storeId or u.storeId=:storeId")
    Page<User> findAllByStoreId(long storeId, Pageable pageable);

    Page<User> findAllByDepartmentId(long departmentId, Pageable pageable);
}

package com.example.marketing.repository;

import com.example.marketing.model.dto.UserDTO;
import com.example.marketing.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);

    User findUserById(Long id);

    @Query("select u from User u order by u.createdAt desc ")
    Page<User> findUserOrderByCreatedDt(Pageable pageable);

    @Query("select u from User  u where (:departmentId is null or u.departmentId=:departmentId) " +
            "and u.status =:status order by u.createdAt desc ")
    Page<User> findAllByDepartmentIdAndStatusOrderByCreatedAtDesc(Long departmentId, boolean status, Pageable pageable);

    @Query("select new com.example.marketing.model.dto.UserDTO" +
            "(u.id, u.username, u.admin, u.email, d.name, u.createdAt) from User  u " +
            "left join Department  d on u.departmentId= d.id where (:departmentId is null or u.departmentId=:departmentId) " +
            "and u.status =true order by u.createdAt desc ")
    Page<UserDTO> findAllByDepartmentIdOrderByCreatedAtDesc(Long departmentId, Pageable pageable);
}

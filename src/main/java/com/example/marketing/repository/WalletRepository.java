package com.example.marketing.repository;

import com.example.marketing.model.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    /*@Query("select new com.example.be_project.model.dto.UserWalletDTO(u.fullName, u.positionId, w.name, w.amount) " +
            "from User u " +
            "JOIN Wallet w ON w.userId= u.id " +
            "where u.storeId =:storeId")
    Page<UserWalletDTO> findAllWallets(@Param("storeId") long storeId, Pageable pageable);*/

    /*@Query("select new com.example.be_project.model.dto.UserWalletDTO(w.name) " +
            "from User u " +
            "JOIN Wallet w ON w.userId= u.id " +
            "where u.storeId =:storeId")
    List<UserWalletDTO> findAllWallets(@Param("storeId") long storeId);*/
}

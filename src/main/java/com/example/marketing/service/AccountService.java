package com.example.marketing.service;

import com.example.marketing.model.entities.User;
import com.example.marketing.model.entities.Wallet;
import com.example.marketing.repository.UserRepository;
import com.example.marketing.repository.WalletRepository;
import com.example.marketing.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public String createUser(User user, String createdBy){
        User userNew = new User();
        try{
            //check user
            userService.loadUserByUsername(user.getUsername());
            return "Username đã tồn tại";
        } catch (UsernameNotFoundException e){
            userNew.setAdmin(userNew.isAdmin());
            userNew.setEmail(user.getEmail());
            userNew.setUsername(user.getUsername());
            userNew.setFullName(user.getFullName());
            userNew.setStatus(Status.ACTIVE);
            userNew.setPhone(user.getPhone());
            userNew.setDepartmentId(user.getDepartmentId());
            userNew.setScriptId(user.getScriptId());
            userNew.setPassword(passwordEncoder.encode(user.getPassword()));
            userNew.setCreatedBy(createdBy);
            userNew.setStoreId(user.getStoreId());
            userRepository.save(userNew);
            Wallet wallet = new Wallet();
            wallet.setUserId(userNew.getId());
            wallet.setAmount(new BigDecimal(0));
            wallet.setName("Ví của "+user.getUsername());
            walletRepository.save(wallet);
            return "00" ;
        } catch (Exception e){
            return "Somethings wrong!";
        }
    }
}

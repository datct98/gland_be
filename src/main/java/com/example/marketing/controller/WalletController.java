package com.example.marketing.controller;
import com.example.marketing.repository.WalletRepository;
import com.example.marketing.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marketing/api-wallet")
@Slf4j
public class WalletController {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private WalletRepository walletRepository;

    /*@CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/all")
    public ResponseEntity<?> getWallets(@RequestHeader(name="Authorization") String token,
                                       @RequestParam(required = false) long storeId,
                                       @RequestParam(required = false) int pageNum) throws Exception {
        if(jwtUtil.validateToken(token.replace("Bearer ",""))){
            Page<UserWalletDTO> walletPage = walletRepository.findAllWallets(storeId, PageRequest.of(pageNum, 10));
            return ResponseEntity.ok(walletPage.getContent());
        }
        throw new Exception("Un-authentication");
    }*/

    /*@CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("")
    public ResponseEntity<?> getWalletsWithoutPage(@RequestHeader(name="Authorization") String token,
                                        @RequestParam(required = false) long storeId) throws Exception {
        if(jwtUtil.validateToken(token.replace("Bearer ",""))){
            List<UserWalletDTO> wallets = walletRepository.findAllWallets(storeId);
            return ResponseEntity.ok(wallets);
        }
        throw new Exception("Un-authentication");
    }*/
}

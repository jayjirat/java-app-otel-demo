package com.example.test_opentelemrtry.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test_opentelemrtry.aop.Traceable;
import com.example.test_opentelemrtry.dto.TransferRequestDto;
import com.example.test_opentelemrtry.dto.TransferResponseDto;
import com.example.test_opentelemrtry.models.User;
import com.example.test_opentelemrtry.services.MyService;

@RestController
public class MyController {
    final private MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

    @Traceable
    @GetMapping("/api/users")
    public ResponseEntity<?> getUsers() {
        try {
            return ResponseEntity.ok(myService.getUsers());
        } catch (Exception e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @Traceable
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
        myService.registerUser(user);
        return ResponseEntity.ok(user);
        } catch (Exception e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    @Traceable
    @PostMapping("/transfer")
    public ResponseEntity<?> transferBalance(@RequestBody List<TransferRequestDto> transferDtos) {
        TransferResponseDto response = new TransferResponseDto();
        try {
            String transactionId = myService.transferBalance(transferDtos);

            response.setStatus("success");
            response.setMessage(null);
            response.setTransactionId(transactionId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            response.setStatus("error");
            response.setMessage(e.getMessage());
            response.setTransactionId(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

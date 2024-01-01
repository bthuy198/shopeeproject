package com.thuy.shopeeproject.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.CompleteOrderReqDTO;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/bills")
public class BillAPI {

    @GetMapping("/get-bill")
    public ResponseEntity<?> getBill(@RequestBody CompleteOrderReqDTO completeOrderReqDTO, HttpServletRequest request) {

        return null;
    }

}

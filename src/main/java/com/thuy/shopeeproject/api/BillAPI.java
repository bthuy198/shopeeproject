package com.thuy.shopeeproject.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuy.shopeeproject.domain.dto.BillResDTO;
import com.thuy.shopeeproject.domain.dto.CompleteOrderReqDTO;
import com.thuy.shopeeproject.domain.entity.Bill;
import com.thuy.shopeeproject.service.IBillService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/users/bills")
public class BillAPI {

    @Autowired
    private IBillService billService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllBills() {
        List<Bill> bills = billService.findAll();
        List<BillResDTO> billResDTOs = new ArrayList<>();
        for (Bill bill : bills) {
            BillResDTO billResDTO = bill.toBillResDTO();
            billResDTOs.add(billResDTO);
        }
        return new ResponseEntity<>(billResDTOs, org.springframework.http.HttpStatus.OK);
    }

}

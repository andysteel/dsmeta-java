package com.gmail.andersoninfonet.dsmeta.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.andersoninfonet.dsmeta.entities.Sale;
import com.gmail.andersoninfonet.dsmeta.services.SaleService;
import com.gmail.andersoninfonet.dsmeta.services.SmsService;

@RestController
@RequestMapping("/sales")
public class SaleController {
    
    private final SaleService saleService;
    private final SmsService smsService;

    /**
     * @param saleService
     */
    public SaleController(SaleService saleService, SmsService smsService) {
        this.saleService = saleService;
        this.smsService = smsService;
    }

    @GetMapping
    public Page<Sale> findSales(
        @RequestParam(value = "minDate", defaultValue = "") String minDate,
        @RequestParam(value = "maxDate", defaultValue = "") String maxDate,
        Pageable pageable) {
        return saleService.findSales(minDate, maxDate, pageable); 
    }

    @GetMapping("/{id}/notification")
    public void notifySms(@PathVariable("id") Long id) {
        smsService.sendSms(id);
    }
}

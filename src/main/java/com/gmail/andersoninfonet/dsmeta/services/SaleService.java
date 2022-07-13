package com.gmail.andersoninfonet.dsmeta.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gmail.andersoninfonet.dsmeta.entities.Sale;
import com.gmail.andersoninfonet.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {
    
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Page<Sale> findSales(String minDate, String maxDate, Pageable pageable) {

        var min = minDate.equals("") ? LocalDate.now().minusYears(1) : LocalDate.parse(minDate);
        var max = maxDate.equals("") ? LocalDate.now() : LocalDate.parse(maxDate);
        return saleRepository.findSales(min, max, pageable);
    }
}

package com.konex.medicines.sales;

import java.util.List;

import org.springframework.stereotype.Service;

import com.konex.medicines.sales.interfaces.SaleRepository;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

  @Resource
  SaleRepository _saleRpt;

  @Transactional
  public List<SaleEntity> getSales() {
    return (List<SaleEntity>) this._saleRpt.findAll();
  }

  @Transactional
  public SaleEntity save(SaleEntity data) {
    return this._saleRpt.save(data);
  }

  @Transactional
  public List<SaleEntity> findBewteenDates(Long start, Long end) {
    return this._saleRpt.findBewteenDates(start, end);
  }

  @Transactional(readOnly = true)
  public SaleEntity findById(Long id) {
    return this._saleRpt.findById(id).orElse(null);
  }
}

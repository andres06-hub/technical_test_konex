package com.konex.medicines.products;

import java.util.List;

import org.springframework.stereotype.Service;

import com.konex.medicines.common.dto.ReturnTypeDto;
import com.konex.medicines.products.interfaces.ProdInterface;
import com.konex.medicines.products.interfaces.ProdRepository;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProdService implements ProdInterface {

  @Resource
  ProdRepository _prodRept;

  @Transactional(readOnly = true)
  @Override
  public List<ProdEntity> getProducts() {
    return (List<ProdEntity>) this._prodRept.findAll();
  }
  
  @Transactional(readOnly = true)
  @Override
  public ProdEntity findById(Long id) {
    return this._prodRept.findById(id).orElse(null);
  }

  @Transactional
  @Override
  public ReturnTypeDto saveProd(ProdEntity data) {
    ProdEntity foundProd = this._prodRept.findByPublicId(data.getPublicId());
    if (foundProd != null ) return new ReturnTypeDto(true, foundProd);
    ProdEntity prod = this._prodRept.save(data);
    log.info("Product Created");
    return new ReturnTypeDto(false, prod); 
  }

  @Transactional
  @Override
  public ReturnTypeDto updateProd(Long id, ProdEntity data) {
    ProdEntity medicine = this.findById(id);
    if (medicine == null) return new ReturnTypeDto(false);
    medicine.setName(data.getName());
    medicine.setFactoryLaboratory(data.getFactoryLaboratory());
    medicine.setDateManufacture(data.getDateManufacture());
    medicine.setExpirationDate(data.getExpirationDate());
    medicine.setQuantityStock(data.getQuantityStock());
    medicine.setUnitValue(data.getUnitValue());
    log.info("Updated Medicine");
    return new ReturnTypeDto(true, medicine, true);
  }

  @Transactional
  @Override
  public ReturnTypeDto deleteProd(Long id) {
    ProdEntity foundProd = this.findById(id);
    if (foundProd == null ) return new ReturnTypeDto(false);
    this._prodRept.deleteById(id);
    log.info("DELETED USER.");
    return new ReturnTypeDto(true); 
  }

  @Transactional(readOnly = true)
  public ProdEntity findByPublicId(String publicId) {
    return this._prodRept.findByPublicId(publicId);
  }

}

package com.konex.medicines.products.interfaces;

import java.util.List;

import com.konex.medicines.common.dto.ReturnTypeDto;
import com.konex.medicines.products.ProdEntity;

public interface ProdInterface {
  
  public List<ProdEntity> getProducts();
  public ProdEntity findById(Long id);
  public ReturnTypeDto saveProd(ProdEntity data);
  public ReturnTypeDto updateProd(Long id, ProdEntity data);
  public ReturnTypeDto deleteProd(Long id);
}

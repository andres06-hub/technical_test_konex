package com.konex.medicines.common.dto;

import com.konex.medicines.products.ProdEntity;

import lombok.Data;

@Data
public class ReturnTypeDto {
  
  public Boolean status;
  public Boolean exists;
  public ProdEntity data = null;

  public ReturnTypeDto(ProdEntity data){
    this.data = data;
  }

  public ReturnTypeDto(Boolean exists){
    this.exists = exists;
  }

  public ReturnTypeDto(Boolean exists, ProdEntity data){
    this.exists = exists;
    this.data = data;
  }

    public ReturnTypeDto(Boolean status, ProdEntity data, Boolean exists){
    this.status = status;
    this.data = data;
    this.exists = exists;
  }
  
}

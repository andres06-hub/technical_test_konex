package com.konex.medicines.common.dto;

import lombok.Data;

@Data
public class ResponseSaleDto {

  private String msg;
  private Long idSale;
  private String publicId;
  private String name;
  private Integer quantityPurchased;
  private Double totalValue;

  public ResponseSaleDto(String msg) {
    this.msg = msg;
  }

  public ResponseSaleDto(
    String msg, 
    Long idSale,
    String publicId, 
    String name, 
    Integer quantityPurchased, 
    Double totalValue)
    {
      this.msg = msg;
      this.idSale = idSale;
      this.publicId = publicId;
      this.name = name;
      this.quantityPurchased = quantityPurchased;
      this.totalValue = totalValue;
    }

}

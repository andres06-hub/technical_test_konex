package com.konex.medicines.common.dto;


import lombok.Data;

@Data
public class ResponseDto {

  private String message = null;
  private Object data = null;

  public ResponseDto(String message) {
    this.message = message;
  } 

  public ResponseDto(String message, Object data) {
    this.message = message;
    this.data = data;
  }
}

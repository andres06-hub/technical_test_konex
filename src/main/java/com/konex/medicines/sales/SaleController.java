package com.konex.medicines.sales;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import com.konex.medicines.common.dto.ResponseDto;
import com.konex.medicines.common.dto.ResponseSaleDto;
import com.konex.medicines.common.dto.ReturnTypeDto;
import com.konex.medicines.products.ProdEntity;
import com.konex.medicines.products.ProdService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v01/")
public class SaleController {

  @Resource
  ProdService _prodSrv;

  @Resource 
  SaleService _saleSrv;

  @PostMapping("sale")
  public ResponseEntity<ResponseSaleDto> createSale(@RequestBody SaleEntity data) {
      // validate if the product has sufficient quantity
      ProdEntity medicine = this._prodSrv.findByPublicId(data.publicIdMedicine);
      if (medicine == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseSaleDto("The drug does not exist"));
      if (medicine.getQuantityStock() <= 0) return ResponseEntity.status(HttpStatus.OK).body(new ResponseSaleDto("There is no products!"));
      if (medicine.getQuantityStock() < data.getQuantity()) return ResponseEntity.status(HttpStatus.OK).body(new ResponseSaleDto("Medicine '"+ data.getName() + "', does not have enough products: Current stock: " + medicine.getQuantityStock()));
      Integer resultStock = medicine.getQuantityStock() - data.getQuantity();
      //Update product Stock
      medicine.setQuantityStock(resultStock);
      ReturnTypeDto result;
      try {
        result = this._prodSrv.updateProd(medicine.getId(), medicine);
      } catch (Exception e) {
        log.error("ERROR:" + e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseSaleDto("ERROR in DB: "+e));
      }
      if (result.getStatus()) log.info("Updated Medicine: " + result.getData());
      // Round value
      double totalValue = data.getQuantity()*data.getUnitPrice();
      //TODO: Cambiar a timeStand
      double finalValue = Math.round( totalValue * 100.0 ) / 100.0;
      data.setTotalValue(finalValue);
      data.setSaleDateTime(data.getSaleDateTime());
      SaleEntity sale;
      //Save Sale
      try {
        sale = this._saleSrv.save(data);
      } catch (Exception e) {
        log.error("ERROR: " + e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseSaleDto("ERROR in DB: " + e));
      }
      log.info("SAVE sale: " + sale);
      return ResponseEntity
            .status(HttpStatus.OK)
            .body(new ResponseSaleDto("Purchase successfully created!",sale.getId(),sale.getPublicIdMedicine(), sale.getName(), sale.getQuantity(), sale.getTotalValue()));
  }

  //TODO: Crear Filtros
  @GetMapping("sales/")
  public ResponseEntity<?> getSales(
    @RequestParam(value = "filterStartDate", defaultValue = "") Long filterStartDate,
    @RequestParam(value = "filterEndDate", defaultValue = "") Long filterEndDate
  ) {
    if (filterStartDate != null && filterEndDate != null) {
      //Obtener ventas por fecha
      List<SaleEntity> sales = this._saleSrv.findBewteenDates(filterStartDate, filterEndDate);
      System.out.println("SALES FILTERS: "+sales);
    }

    List<SaleEntity> sales = this._saleSrv.getSales();
    // Obtener las ventas
    System.out.println(sales);
    return ResponseEntity.status(HttpStatus.OK).body("");
  }

  //API para calcular el valor total de una compra
  
}

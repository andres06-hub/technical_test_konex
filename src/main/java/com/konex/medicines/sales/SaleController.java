package com.konex.medicines.sales;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

import com.konex.medicines.common.dto.ResponseDto;
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
  public ResponseEntity<ResponseDto> createSale(@RequestBody SaleEntity data) {
      // validate if the product has sufficient quantity
      ProdEntity medicine = this._prodSrv.findByPublicId(data.publicIdMedicine);
      if (medicine == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("The drug does not exist"));
      if (medicine.getQuantityStock() <= 0) return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("There is no products!"));
      if (medicine.getQuantityStock() < data.getQuantity()) return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Medicine '"+ data.getName() + "', does not have enough products: Current stock: " + medicine.getQuantityStock()));
      Integer resultStock = medicine.getQuantityStock() - data.getQuantity();
      //Update product Stock
      medicine.setQuantityStock(resultStock);
      ReturnTypeDto result;
      try {
        result = this._prodSrv.updateProd(medicine.getId(), medicine);
      } catch (Exception e) {
        log.error("ERROR:" + e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("ERROR in DB: "+e));
      }
      if (result.getStatus()) log.info("Updated Medicine: " + result.getData());
      //Save Sale
      data.setTotalValue(data.getQuantity()*data.getUnitPrice());
      String dataTime = this._saleSrv.generateDateTime();
      data.setSaleDateTime(dataTime);
      SaleEntity sale;
      try {
        sale = this._saleSrv.save(data);
      } catch (Exception e) {
        log.error("ERROR: " + e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("ERROR in DB: ", e));
      }
      log.info("SAVE sale: " + sale);
      //TODO: crear clase para la respuesta de la compra, los datos de la compara
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Purchase successfully created!"));
  }

  //Filtrar
  @GetMapping("sale")
  public ResponseEntity<?> getSales() {
    // Obtener las ventas
    return ResponseEntity.status(HttpStatus.OK).body("");
  }

  //API para calcular el valor total de una compra
  
}

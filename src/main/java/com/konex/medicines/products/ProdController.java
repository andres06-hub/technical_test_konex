package com.konex.medicines.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.konex.medicines.common.constants.Constants;
import com.konex.medicines.common.dto.ResponseDto;
import com.konex.medicines.common.dto.ReturnTypeDto;
import com.konex.medicines.common.services.RedisService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v01/")
public class ProdController {

  private Constants _const = new Constants();

  @Resource
  ProdService _prodService;

  @Resource 
  RedisService _redisSrv;

  @GetMapping("medicines")
  public ResponseEntity<ResponseDto> getAll() throws JsonMappingException, JsonProcessingException {
    List<Object> data = this._redisSrv.getData(_const.KEY);
    System.out.println("--> "+data+"\n");
    if (data != null && !data.isEmpty()) {
      // Transform data
      List<ProdEntity> medicines = this._redisSrv.covertJsonToObjectClass(data);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Medicines", medicines));
    }
    List<ProdEntity> results = this._prodService.getProducts();
    System.out.println(!results.isEmpty() ? results : "No products!");
    //Save Products: Medicines
    try {
      this._redisSrv.saveData(_const.KEY, results);
    } catch (Exception e) {
      log.error("ERROR: ", e);
    }
    return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseDto("MEDICINES:", results));
  }

  //TODO: CREAR FILTROS
  @GetMapping("medicines/filter")
  public ResponseEntity<ResponseDto> getMedicinesFilter(
    @RequestParam(value = "filterBy", defaultValue = "") String filterBy,
    @RequestParam(value= "value", defaultValue = "") String value
  ) throws JsonMappingException, JsonProcessingException {

    //Hacer filtros con swicht
    switch (filterBy) {
      case "name":
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Medicines", this._prodService.findByName(value)));
      case "factoryLaboratory":
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Medicines", this._prodService.findByFactory(value)));
    }
    System.out.println(filterBy+" - "+value);
    return ResponseEntity.status(HttpStatus.OK)
          .body(new ResponseDto(""));
  }

  @PutMapping("medicine/{id}")
  public ResponseEntity<ResponseDto> putProduct(@PathVariable Long id, @RequestBody @Valid ProdEntity data) {
    ReturnTypeDto result = this._prodService.updateProd(id,data);
    if(result.getExists() == false) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Not found drug"));
    return ResponseEntity.status(HttpStatus.OK)
              .body(new ResponseDto("Updated Medicine", result.data));
  }

  @DeleteMapping("medicine/{id}")
  public ResponseEntity<ResponseDto> delProduct(@PathVariable("id") String id) {
    Long _id = (long) Integer.parseInt(id);
    ReturnTypeDto result = this._prodService.deleteProd(_id);
    if (result.getExists() == false) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto("Drug does not exist!", null));
    return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseDto("Drug Deleted", null));
  }

  //TODO:transformar el name a minuscula
  @PostMapping("medicine")
  public ResponseEntity<ResponseDto> createProd(@RequestBody @Valid ProdEntity data) {
    ReturnTypeDto result = this._prodService.saveProd(data);
    System.out.println(result);
    if (result.getExists() == true) return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Drug Exists", null));
    this._redisSrv.deleteValue(_const.KEY);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto("Drug created!", result.getData()));
  }

  @PostMapping("medicine/create-products")
  public ResponseEntity<ResponseDto> createProducts(@RequestBody @Valid List<ProdEntity> data) {
    System.out.println(data);
    try {
      this._prodService.saveProducts(data);
    } catch (Exception e) {
      log.error("ERROR: " + e);
    }
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("OK"));
  }
}

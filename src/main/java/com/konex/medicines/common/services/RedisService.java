package com.konex.medicines.common.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konex.medicines.products.ProdEntity;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisService {

  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  public List<Object> getData(String key) {
    log.info("Get Redis Data");
    return this.redisTemplate.opsForList().range(key, 0, -1);
  }

  public void saveData(String key, List<ProdEntity> data) throws JsonProcessingException {
    log.info("Save Data in Redis");
    List<String> jsonList = this.getJsonList(data);
    //Validate if the key exists
    if (this.redisTemplate.hasKey(key)) {
      try {
        this.deleteValue(key);
      } catch (Exception e) {
        log.error("ERROR:", e);
        throw new Error("ERROR: Deleleting data");
      }
    }
    // SAVE values by list
    for (String _jsonProd : jsonList) {
      String jsonProd = _jsonProd.toString();
      this.redisTemplate.opsForList().rightPush(key, jsonProd);
    }
  }

  public Boolean deleteValue(String key) {
    log.info("Delete Data in Redis");
    return this.redisTemplate.delete(key);
  }

  //Convert Object to Json
  public List<String> getJsonList(List<ProdEntity> data) throws JsonProcessingException {
    List<String> jsonList = new ArrayList<String>();
    ObjectMapper mapper = new ObjectMapper();
    for (ProdEntity ele : data) {
      String prodJson = mapper.writeValueAsString(ele);
      jsonList.add(prodJson);
    }
    System.out.println("\nJSON:: "+jsonList);
    return jsonList;
  }

  // Convert Json to ObjectClass ProdEntity
  public List<ProdEntity> covertJsonToObjectClass(List<Object> data) throws JsonMappingException, JsonProcessingException {
    List<ProdEntity> medicines = new ArrayList<ProdEntity>();
    ObjectMapper mapper = new ObjectMapper();
    for (Object json : data) {
      String _json = json.toString();
      ProdEntity medicine = mapper.readValue(_json, ProdEntity.class);
      medicines.add(medicine);
    }
    return medicines;
  }
}

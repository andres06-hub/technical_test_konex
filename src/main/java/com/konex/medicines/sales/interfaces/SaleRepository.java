package com.konex.medicines.sales.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.konex.medicines.sales.SaleEntity;

public interface SaleRepository extends CrudRepository<SaleEntity, Long>{
  @Query("SELECT s FROM SaleEntity s WHERE s.saleDateTime>=?1 AND s.saleDateTime<=?2")
  public List<SaleEntity> findBewteenDates(Long start, Long end);
}

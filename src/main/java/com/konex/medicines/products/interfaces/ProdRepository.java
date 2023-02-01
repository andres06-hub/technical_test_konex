package com.konex.medicines.products.interfaces;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.konex.medicines.products.ProdEntity;

public interface ProdRepository extends CrudRepository<ProdEntity, Long> {
  @Query("SELECT p FROM ProdEntity p WHERE p.publicId = ?1")
  public ProdEntity findByPublicId(String publicId);

  @Query("SELECT p FROM ProdEntity p WHERE p.name = ?1")
  public Iterable<ProdEntity> findByName(String name);

  @Query("SELECT p FROM ProdEntity p WHERE p.factoryLaboratory = ?1")
  public Iterable<ProdEntity> findByFactory(String factoryLaboratory);
}

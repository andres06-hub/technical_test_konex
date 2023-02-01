package com.konex.medicines.products;

import java.io.Serializable;
import java.util.List;

import com.konex.medicines.sales.SaleEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class ProdEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  Long id;

  @Column(name = "public_id", nullable = false)
  @NotBlank
  @Size(min = 2, max = 20)
  String publicId;

  @Column(name = "name", nullable = false)
  @NotBlank(message = "Name is mandatory!")
  @Size(message = "Name many characters", min = 2, max = 180)
  String name;

  @Column(name = "factory_laboratory", nullable = false)
  @NotBlank(message = "Factory_Laboratory is mandatory!")
  @Size(message = "Factory_Laboratory many characters", min = 2, max = 255)
  String factoryLaboratory;

  @Column(name = "date_manufacture", nullable = false)
  @NotNull(message = "Manufacture_date is mandatory!")
  Long dateManufacture;

  @Column(name = "expiration_date", nullable = false)
  @NotNull(message = "Expiration_date in mandatory")
  Long expirationDate;

  @Column(name = "quantity_stock", nullable = false)
  @NotNull(message = "Stock cannot be Null")
  Integer quantityStock;

  @Column(name = "unit_value", nullable = false)
  @NotNull(message = "value cannot be Null")
  Double unitValue;

  // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  // private List<SaleEntity> sales;
}

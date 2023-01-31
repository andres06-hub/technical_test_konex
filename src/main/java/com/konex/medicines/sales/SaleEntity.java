package com.konex.medicines.sales;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "sale")
public class SaleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "public_id_medicine")
    String publicIdMedicine;

    @Column(name = "name")
    String name;

    @Column(name = "sale_datetime")
    @NotNull
    String saleDateTime;

    @Column(name = "quantity")
    @NotNull
    Integer quantity;

    @Column(name = "unit_price")
    Double unitPrice;

    @Column(name = "total_value")
    @NotNull
    Double totalValue;
}
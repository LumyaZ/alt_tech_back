package com.shop.producttrialmaster.dto;

import com.shop.producttrialmaster.enums.InventoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private String image;

    private String category;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    private String internalReference;

    private Long shellId;

    private InventoryStatus inventoryStatus;

    private Double rating;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
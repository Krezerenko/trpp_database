package io.github.krezerenko.trpp_database.api.product;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false, name = "image_path")
    private String imagePath;
}

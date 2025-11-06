package com.ecommerse.catalogo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_changes")
@Builder
public class ProductChange {

    @Id
    private String id; // cambié Long a String para usar ObjectId de Mongo
    private String productId;
    private LocalDateTime changeDate;
    private String modifiedFieldsJson;
    private String changeType;

}

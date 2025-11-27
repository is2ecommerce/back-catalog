package com.ecommerse.catalogo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "product_changes")
public class ProductChange {
    @Id
    private String id;
    private String productId;
    private String changeType; // UPDATE, DELETE, CREATE
    private LocalDateTime changeDate;
    private String modifiedFieldsJson; // Snapshot o JSON de cambios

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public LocalDateTime getChangeDate() { return changeDate; }
    public void setChangeDate(LocalDateTime changeDate) { this.changeDate = changeDate; }
    public String getModifiedFieldsJson() { return modifiedFieldsJson; }
    public void setModifiedFieldsJson(String modifiedFieldsJson) { this.modifiedFieldsJson = modifiedFieldsJson; }
}

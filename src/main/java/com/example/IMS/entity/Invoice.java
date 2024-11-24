package com.example.IMS.entity;

import com.example.IMS.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double paidAmount = 0.0;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private String status = InvoiceStatus.PENDING.getValue();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // This method ensures the defaults are set before persisting
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // Set default createdAt value
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now(); // Set default updatedAt value
        }
        if (this.paidAmount == null) {
            this.paidAmount = 0.0; // Set default paidAmount value if not already set
        }
        if (this.status == null) {
            this.status = InvoiceStatus.PENDING.getValue(); // Set default status value
        }
    }
}

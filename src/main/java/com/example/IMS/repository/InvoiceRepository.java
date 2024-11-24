package com.example.IMS.repository;

import com.example.IMS.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    List<Invoice> findByStatusAndDueDateBefore(String value, LocalDate localDate);
}

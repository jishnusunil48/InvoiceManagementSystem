package com.example.IMS.service;

import com.example.IMS.enums.InvoiceStatus;
import com.example.IMSLibrary.requestDto.InvoiceDto;
import com.example.IMS.entity.Invoice;
import com.example.IMS.exception.ResourceNotFoundException;
import com.example.IMS.repository.InvoiceRepository;
import com.example.IMS.mapper.InvoiceMapper;
import com.example.IMSLibrary.requestDto.OverdueProcessingDto;
import com.example.IMSLibrary.requestDto.PaymentRequestDto;
import com.example.IMSLibrary.responseDto.InvoiceResponseDto;
import com.example.IMSLibrary.responseDto.OverdueProcessingResponseDto;
import com.example.IMSLibrary.responseDto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public Map<String, Long> createInvoice(InvoiceDto invoiceDto) {
        log.info("Entering createInvoice service method with invoiceDto: {}", invoiceDto);
        try {
            Invoice invoice = invoiceMapper.invoiceDtoToInvoice(invoiceDto);
            log.debug("Converted InvoiceDto to Invoice entity: {}", invoice);

            Invoice savedInvoice = invoiceRepository.save(invoice);
            log.info("Invoice saved successfully with id: {}", savedInvoice.getId());

            return Collections.singletonMap("id", savedInvoice.getId());
        } catch (Exception e) {
            log.error("Error occurred while creating invoice: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create invoice, please try again.");
        }
    }

    @Override
    public List<InvoiceResponseDto> getAllInvoices() {
        log.info("Entering getAllInvoices service method");
        try {
            List<InvoiceResponseDto> invoices = invoiceRepository.findAll().stream()
                    .map(invoiceMapper::invoiceToInvoiceResponseDto)
                    .collect(Collectors.toList());
            log.info("Fetched {} invoices", invoices.size());
            return invoices;
        } catch (Exception e) {
            log.error("Error occurred while fetching invoices: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch invoices, please try again.");
        }
    }

    public PaymentResponseDto processPayment(Long invoiceId, PaymentRequestDto paymentRequestDto) {
        log.info("Processing payment for invoiceId: {}", invoiceId);
        try {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> {
                        log.error("Invoice with id {} not found", invoiceId);
                        return new ResourceNotFoundException("Invoice with id " + invoiceId + " not found");
                    });

            Double newPaidAmount = invoice.getPaidAmount() + paymentRequestDto.getAmount();
            invoice.setPaidAmount(newPaidAmount);

            if (newPaidAmount >= invoice.getAmount()) {
                invoice.setStatus(InvoiceStatus.PAID.getValue());
                log.info("Invoice with id {} marked as PAID", invoiceId);
            }

            invoiceRepository.save(invoice);

            return PaymentResponseDto.builder()
                    .invoiceId(invoice.getId())
                    .paidAmount(newPaidAmount)
                    .status(invoice.getStatus())
                    .message("Payment processed successfully.")
                    .build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process payment, please try again.");
        }
    }

    @Override
    public OverdueProcessingResponseDto processOverdueInvoices(OverdueProcessingDto overdueProcessingDto) {
        log.info("Processing overdue invoices with criteria: {}", overdueProcessingDto);
        try {
            List<Invoice> overdueInvoices = invoiceRepository.findByStatusAndDueDateBefore(
                    InvoiceStatus.PENDING.getValue(),
                    LocalDate.now().minusDays(overdueProcessingDto.getOverdueDays())
            );

            log.info("Found {} overdue invoices to process", overdueInvoices.size());

            List<Long> newInvoiceIds = overdueInvoices.stream()
                    .map(invoice -> processInvoice(invoice, overdueProcessingDto))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("Processed {} overdue invoices successfully", newInvoiceIds.size());

            return OverdueProcessingResponseDto.builder()
                    .processedInvoicesCount(overdueInvoices.size())
                    .newInvoiceIds(newInvoiceIds)
                    .message("Overdue invoices processed successfully.")
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing overdue invoices: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process overdue invoices, please try again.");
        }
    }

    private Long processInvoice(Invoice invoice, OverdueProcessingDto overdueProcessingDto) {
        log.debug("Processing invoice id {} for overdue processing", invoice.getId());
        try {
            Double totalAmount = invoice.getAmount() + overdueProcessingDto.getLateFee();
            Invoice newInvoice = Invoice.builder()
                    .amount(totalAmount)
                    .dueDate(LocalDate.now().plusDays(overdueProcessingDto.getOverdueDays()))
                    .status(InvoiceStatus.PENDING.getValue())
                    .build();

            if (invoice.getPaidAmount() < invoice.getAmount()) {
                invoice.setStatus(InvoiceStatus.PAID.getValue());
                log.debug("Invoice id {} marked as PAID", invoice.getId());
            } else {
                invoice.setStatus(InvoiceStatus.VOID.getValue());
                log.debug("Invoice id {} marked as VOID", invoice.getId());
            }

            invoiceRepository.save(invoice);

            Invoice savedNewInvoice = invoiceRepository.save(newInvoice);
            log.info("New overdue invoice created with id {}", savedNewInvoice.getId());
            return savedNewInvoice.getId();
        } catch (Exception e) {
            log.error("Error occurred while processing invoice id {}: {}", invoice.getId(), e.getMessage(), e);
            return null;
        }
    }
}

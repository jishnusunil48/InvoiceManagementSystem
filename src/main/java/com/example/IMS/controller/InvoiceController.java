package com.example.IMS.controller;


import com.example.IMS.service.InvoiceService;
import com.example.IMSLibrary.requestDto.InvoiceDto;
import com.example.IMSLibrary.requestDto.OverdueProcessingDto;
import com.example.IMSLibrary.requestDto.PaymentRequestDto;
import com.example.IMSLibrary.responseDto.InvoiceResponseDto;
import com.example.IMSLibrary.responseDto.OverdueProcessingResponseDto;
import com.example.IMSLibrary.responseDto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.IMS.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing invoices.
 *
 * Provides endpoints for creating invoices, retrieving all invoices,
 * processing payments for specific invoices, and handling overdue invoices.
 * Uses {@link InvoiceService} for business logic.
 *
 * All responses are wrapped in {@link ResponseEntity} for proper HTTP status codes.
 */

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    /**
     * Creates a new invoice based on the provided details.
     *
     * @param invoiceDto the details of the invoice to be created, provided as a JSON payload.
     * @return ResponseEntity containing a map with the generated invoice ID and HTTP status code 201 (Created).
     *
     * Example Request Body:
     * {
     *   "customerName": "John Doe",
     *   "amount": 500.0,
     *   "dueDate": "2024-12-01"
     * }
     *
     * Example Response:
     * {
     *   "id": 101
     * }
     */
    @PostMapping
    public ResponseEntity<Map<String, Long>> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        Map<String, Long> response = invoiceService.createInvoice(invoiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all invoices available in the system.
     *
     * @return ResponseEntity containing a list of {@link InvoiceResponseDto} and HTTP status code 200 (OK).
     *
     * Example Response:
     * [
     *   {
     *     "id": 101,
     *     "customerName": "John Doe",
     *     "amount": 500.0,
     *     "status": "PENDING",
     *     "dueDate": "2024-12-01"
     *   },
     *   {
     *     "id": 102,
     *     "customerName": "Jane Smith",
     *     "amount": 700.0,
     *     "status": "PAID",
     *     "dueDate": "2024-11-15"
     *   }
     * ]
     */
    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    /**
     * Processes a payment for a specific invoice.
     *
     * @param invoiceId the ID of the invoice for which the payment is being made, passed as a path variable.
     * @param paymentRequestDto the payment details, including the amount being paid, provided as a JSON payload.
     * @return ResponseEntity containing {@link PaymentResponseDto} with updated payment details and HTTP status code 200 (OK).
     *
     * Example Path Variable:
     * /invoices/101/payments
     *
     * Example Request Body:
     * {
     *   "amount": 200.0
     * }
     *
     * Example Response:
     * {
     *   "invoiceId": 101,
     *   "paidAmount": 200.0,
     *   "status": "PARTIAL",
     *   "message": "Payment processed successfully."
     * }
     *  * Throws:
     *  * - {@link ResourceNotFoundException} if the specified invoice ID does not exist.
     */
    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<PaymentResponseDto> makePayment(@PathVariable Long invoiceId,
                                                          @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponseDto = invoiceService.processPayment(invoiceId, paymentRequestDto);
        return ResponseEntity.ok(paymentResponseDto);
    }

    /**
     * Processes overdue invoices based on the specified criteria.
     *
     * Marks invoices as overdue and creates new invoices with late fees if applicable.
     *
     * @param overdueProcessingDto the criteria for processing overdue invoices, including overdue days and late fee details.
     * @return ResponseEntity containing {@link OverdueProcessingResponseDto} with details of processed invoices and HTTP status code 200 (OK).
     *
     * Example Request Body:
     * {
     *   "overdueDays": 30,
     *   "lateFee": 50.0
     * }
     *
     * Example Response:
     * {
     *   "processedInvoicesCount": 5,
     *   "newInvoiceIds": [201, 202, 203],
     *   "message": "Overdue invoices processed successfully."
     * }
     */
    @PostMapping("/process-overdue")
    public ResponseEntity<OverdueProcessingResponseDto> processOverdueInvoices(@RequestBody OverdueProcessingDto overdueProcessingDto) {
        OverdueProcessingResponseDto response = invoiceService.processOverdueInvoices(overdueProcessingDto);
        return ResponseEntity.ok(response);
    }

}

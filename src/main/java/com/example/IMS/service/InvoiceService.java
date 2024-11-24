package com.example.IMS.service;


import com.example.IMSLibrary.requestDto.InvoiceDto;
import com.example.IMSLibrary.requestDto.OverdueProcessingDto;
import com.example.IMSLibrary.requestDto.PaymentRequestDto;
import com.example.IMSLibrary.responseDto.InvoiceResponseDto;
import com.example.IMSLibrary.responseDto.OverdueProcessingResponseDto;
import com.example.IMSLibrary.responseDto.PaymentResponseDto;

import java.util.List;
import java.util.Map;

public interface InvoiceService {

    public Map<String, Long> createInvoice(InvoiceDto invoiceDto);

    public List<InvoiceResponseDto> getAllInvoices();

    public PaymentResponseDto processPayment(Long invoiceId, PaymentRequestDto paymentRequestDto);

    public OverdueProcessingResponseDto processOverdueInvoices(OverdueProcessingDto overdueProcessingDto);
}

package com.example.IMS.mapper;

import com.example.IMSLibrary.requestDto.InvoiceDto;
import com.example.IMS.entity.Invoice;
import com.example.IMSLibrary.responseDto.InvoiceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceResponseDto invoiceToInvoiceResponseDto(Invoice invoice);

    @Mapping(source = "due_date", target = "dueDate")
    Invoice invoiceDtoToInvoice(InvoiceDto invoiceDto);
}

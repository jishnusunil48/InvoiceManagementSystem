# Invoice Management System (IMS)

## Overview

The **Invoice Management System (IMS)** is a Spring Boot-based application that helps manage invoices efficiently. It provides functionality for creating invoices, retrieving all invoices, processing payments, and handling overdue invoices. The application utilizes RESTful APIs, PostgreSQL as the database, and employs best practices for logging, exception handling, and data processing.

---

## Features

- **Create Invoices:** Add new invoices with details like customer name, amount, and due date.
- **Retrieve All Invoices:** Fetch a list of all invoices with their details.
- **Process Payments:** Update payment status for an invoice and calculate the remaining or fully paid amounts.
- **Handle Overdue Invoices:** Identify overdue invoices and process them with applicable late fees.

---

## Technologies Used

- **Java 17**
- **Spring Boot 3.3.4**
- **PostgreSQL**
- **Hibernate** (JPA for ORM)
- **Lombok** for reducing boilerplate code
- **MapStruct** for DTO-Entity mapping
- **SLF4J and Logback** for logging
- **Docker** (Optional: for containerization)

---

## API Endpoints

### Invoice Management

1. **Create Invoice**

    - **URL:** `POST /invoices`
    - **Description:** Creates a new invoice.
    - **Request Body Example:**
      ```json
      {
        "customerName": "John Doe",
        "amount": 500.0,
        "dueDate": "2024-12-01"
      }
      ```
    - **Response Example:**
      ```json
      {
        "id": 101
      }
      ```

2. **Retrieve All Invoices**

    - **URL:** `GET /invoices`
    - **Description:** Fetches all invoices.
    - **Response Example:**
      ```json
      [
        {
          "id": 101,
          "customerName": "John Doe",
          "amount": 500.0,
          "status": "PENDING",
          "dueDate": "2024-12-01"
        },
        {
          "id": 102,
          "customerName": "Jane Smith",
          "amount": 750.0,
          "status": "PAID",
          "dueDate": "2024-11-15"
        }
      ]
      ```

3. **Process Payment**

    - **URL:** `POST /invoices/{invoiceId}/payments`
    - **Description:** Processes payment for a specific invoice.
    - **Request Body Example:**
      ```json
      {
        "amount": 200.0
      }
      ```
    - **Response Example:**
      ```json
      {
        "invoiceId": 101,
        "paidAmount": 200.0,
        "status": "PARTIALLY_PAID",
        "message": "Payment processed successfully."
      }
      ```

4. **Process Overdue Invoices**
    - **URL:** `POST /invoices/process-overdue`
    - **Description:** Processes overdue invoices and applies late fees.
    - **Request Body Example:**
      ```json
      {
        "overdueDays": 30,
        "lateFee": 50.0
      }
      ```
    - **Response Example:**
      ```json
      {
        "processedInvoicesCount": 2,
        "newInvoiceIds": [201, 202],
        "message": "Overdue invoices processed successfully."
      }
      ```

---

## Setup Instructions

### Prerequisites

1. **Java 17**
2. **Maven**
3. **PostgreSQL Database**
4. **Docker (Optional)**
5. **Client Library:** Make sure to clean and install the client library first before starting the application. The DTOs are located in the client library and must be built before running the application.

### Steps to Run

1. Clone the repository.
2. Update `application.properties` with your PostgreSQL configurations.
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -jar target/IMS-0.0.1-SNAPSHOT.jar
   ```

---

## Logging

All logs are written using SLF4J with Logback. The log file can be configured in `logback-spring.xml`.

---

## Error Handling

The application uses a global exception handler to manage errors gracefully and returns appropriate HTTP status codes and messages for client and server errors.

---

## Author
**JISHNU**
**Contact:** 9567507349

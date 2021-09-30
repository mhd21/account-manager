package com.mpakbaz.accountManager.http.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import com.mpakbaz.accountManager.http.validators.DateFormat;
import com.mpakbaz.accountManager.http.validators.SortDir;
import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;
import com.mpakbaz.accountManager.infrastructure.database.repositories.TransactionLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionLogController {

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @GetMapping(path = "/logs")
    public Page<Transaction> getTransactionLog(@Valid @DateFormat @RequestParam String fromDate,
            @Valid @DateFormat @RequestParam String toDate,
            @Valid @SortDir @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @Valid @Min(1) @RequestParam(required = false, defaultValue = "1") int page,
            @Valid @Min(1) @RequestParam(required = false, defaultValue = "10") int perPage) {

        Direction direction = Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "created_at"));

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(com.mpakbaz.accountManager.constants.DateFormat.DATE_FORMAT);

        LocalTime fromTime = LocalTime.of(0, 0, 0);
        LocalTime toTime = LocalTime.of(23, 59, 59);
        LocalDateTime from = LocalDateTime.of(LocalDate.parse(fromDate, formatter), fromTime);
        LocalDateTime to = LocalDateTime.of(LocalDate.parse(toDate, formatter), toTime);

        return this.transactionLogRepository.getTransactionLogs(from, to, pageable);

    }

}

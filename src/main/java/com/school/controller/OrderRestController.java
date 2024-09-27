package com.school.controller;

import com.school.dto.OrderDtoForPurchase;
import com.school.dto.OrderDtoForSave;
import com.school.entity.Order;
import com.school.entity.Order.State;
import com.school.entity.User;
import com.school.service.api.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderRestController implements GlobalController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody OrderDtoForSave order) {
        return orderService.create(order);
    }

    @PostMapping("/purchase")
    @ResponseStatus(HttpStatus.OK)
    public Order.Purchase purchase(@RequestBody OrderDtoForPurchase order) {
        return orderService.purchase(order);
    }

    @GetMapping("/approve/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void approve(@PathVariable UUID id) {
        orderService.approve(id);
    }

    @GetMapping
    public Page<Order> getAll(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable UUID id) {
        return orderService.getById(id);
    }

    @GetMapping("/tutor")
    public Page<Order> getByIdForTutorWithSortParam(@RequestParam UUID id,
                                                    @RequestParam(required = false) User.Subject subject, @RequestParam(required = false) State state,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
                                                    Pageable pageable) {
        Page<Order> filteredOrder = orderService.getByTutorIdWithFilterParams(id, subject, state, startDate, pageable);

        return filteredOrder;
    }

    @GetMapping("/student")
    public Page<Order> getByIdForStudentWithSortParam(@RequestParam UUID id,
                                                      @RequestParam(required = false) User.Subject subject, @RequestParam(required = false) State state,
                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
                                                      Pageable pageable) {
        Page<Order> filteredOrder = orderService.getByStudentIdWithFilterParams(id, subject, state, startDate,
                pageable);

        return filteredOrder;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        orderService.delete(id);
    }
}

package com.school.service.api;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.school.dto.OrderDtoForSave;
import com.school.entity.Order;
import com.school.entity.Order.State;
import com.school.entity.User;

public interface OrderService extends Service<Order, UUID> {
	Order create(OrderDtoForSave dto);

	Page<Order> getByTutorIdWithFilterParams(UUID id, User.Subject subject, State state, LocalDateTime startDate,
			Pageable pageable);

	Page<Order> getByStudentIdWithFilterParams(UUID id, User.Subject subject, State state, LocalDateTime startDate,
			Pageable pageable);

	int approve(UUID id);
}

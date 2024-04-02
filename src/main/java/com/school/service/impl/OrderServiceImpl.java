package com.school.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.school.dto.OrderDtoForSave;
import com.school.entity.Order;
import com.school.entity.Order.State;
import com.school.entity.User;
import com.school.exception.CustomException;
import com.school.exception.ExceptionLocations;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.repository.CourseRepository;
import com.school.repository.OrderRepository;
import com.school.repository.UserRepository;
import com.school.service.api.OrderService;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {
	public static final String KEY_FOR_EXCEPTION_ORDER_NOT_FOUND = "OrderService.OrderNotFound";
	public static final String KEY_FOR_EXCEPTION_STUDENT_NOT_FOUND = "OrderService.StudentNotFound";
	public static final String KEY_FOR_EXCEPTION_TUTOR_NOT_FOUND = "OrderService.TutorNotFound";
	public static final String KEY_FOR_EXCEPTION_COURSE_NOT_FOUND = "OrderService.CourseNotFound";
	public static final String ID_NOT_CORRECT = "OrderService.NotCorrectId";
	public static final String ONLY_TUTOR_CAN_CHANGE_STATE = "OrderService.TutorValidationException";
	public static final String USER_NOT_AUTHENTICATED = "OrderService.UserNotAuthenticated";
	private static final String THERE_IS_NO_ENUM = "OrderService.ThereIsNoEnum";;

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;

	public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
			CourseRepository courseRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public Order getById(UUID id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new CustomException(
						InternalizationMessageManagerConfig.getExceptionMessage(KEY_FOR_EXCEPTION_ORDER_NOT_FOUND),
						ExceptionLocations.ORDER_SERVICE_NOT_FOUND));
	}

	@Override
	public Page<Order> getByTutorIdWithFilterParams(UUID id, User.Subject subject, State state,
			LocalDateTime startDate, Pageable pageable) {
		if (startDate == null) {
			startDate = LocalDateTime.parse("2007-12-03T10:15:30");
		}

		Page<Order> filteredOrders = orderRepository.findTutorsWithParams(id, subject, state, startDate, pageable);

		return filteredOrders;
	}

	@Override
	public Page<Order> getByStudentIdWithFilterParams(UUID id, User.Subject subject, State state,
			LocalDateTime startDate, Pageable pageable) {
		if (startDate == null) {
			startDate = LocalDateTime.parse("2007-12-03T10:15:30");
		}

		Page<Order> filteredOrders = orderRepository.findStudentsWithParams(id, subject, state, startDate, pageable);

		return filteredOrders;
	}

	@Override
	public int approve(UUID id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		int result = 0;
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
			String username = userDetails.getUsername();

			User currentUser = userRepository.findByEmail(username).orElseThrow(() -> new CustomException(
					InternalizationMessageManagerConfig.getExceptionMessage(KEY_FOR_EXCEPTION_STUDENT_NOT_FOUND),
					ExceptionLocations.ORDER_SERVICE_NOT_FOUND));

			if (currentUser.getStatus() != User.Status.TUTOR) {
				throw new CustomException(
						InternalizationMessageManagerConfig.getExceptionMessage(ONLY_TUTOR_CAN_CHANGE_STATE),
						ExceptionLocations.ORDER_SERVICE_NOT_FOUND);
			}

			result = orderRepository.changeStateToApprove(id);

			if (result <= 0) {
				throw new CustomException(
						InternalizationMessageManagerConfig.getExceptionMessage(KEY_FOR_EXCEPTION_ORDER_NOT_FOUND),
						ExceptionLocations.ORDER_SERVICE_NOT_FOUND);
			}
		} else {
			throw new CustomException(InternalizationMessageManagerConfig.getExceptionMessage(USER_NOT_AUTHENTICATED),
					ExceptionLocations.ORDER_SERVICE_NOT_FOUND);
		}

		return result;
	}

	@Override
	public Page<Order> getAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public Order create(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public Order create(OrderDtoForSave dto) {
		Order entity = new Order();

		User student = userRepository.findById(dto.studentId())
				.orElseThrow(() -> new CustomException(
						InternalizationMessageManagerConfig.getExceptionMessage(KEY_FOR_EXCEPTION_STUDENT_NOT_FOUND),
						ExceptionLocations.ORDER_SERVICE_NOT_FOUND));

		User tutor = userRepository.findById(dto.tutorId())
				.orElseThrow(() -> new CustomException(
						InternalizationMessageManagerConfig.getExceptionMessage(KEY_FOR_EXCEPTION_TUTOR_NOT_FOUND),
						ExceptionLocations.ORDER_SERVICE_NOT_FOUND));

		entity.setStudent(student);
		entity.setTutor(tutor);
		entity.setSubject(dto.subject());
		entity.setState(Order.State.SELECTED);
		entity.setStartDate(LocalDateTime.now());

		Order newOrder = orderRepository.save(entity);

		return newOrder;
	}

	@Override
	public void delete(UUID id) {
		orderRepository.deleteById(id);
	}
}

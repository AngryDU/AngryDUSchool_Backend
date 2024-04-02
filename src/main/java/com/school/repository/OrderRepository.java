package com.school.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.school.entity.Order;
import com.school.entity.User;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	@Query(value = "select o from Order o where o.student.id = :student_id")
	Page<Order> findAllForStudent(@Param("student_id") UUID id, Pageable pageable);

	@Query(value = "select o from Order o where " + "o.tutor.id = :tutor_id "
			+ "and (:subject is null or o.subject = :subject) and (:state is null or o.state = :state) "
			+ "and (cast(:startDate as timestamp) is null or o.startDate >= :startDate)")
	Page<Order> findTutorsWithParams(@Param("tutor_id") UUID id, @Param("subject") User.Subject subject,
			@Param("state") Order.State state, @Param("startDate") LocalDateTime startDate, Pageable pageable);

	@Query(value = "select o from Order o where " + "o.student.id = :student_id "
			+ "and (:subject is null or o.subject = :subject) and (:state is null or o.state = :state) "
			+ "and (cast(:startDate as timestamp) is null or o.startDate >= :startDate)")
	Page<Order> findStudentsWithParams(@Param("student_id") UUID id, @Param("subject") User.Subject subject,
			@Param("state") Order.State state, @Param("startDate") LocalDateTime startDate, Pageable pageable);

	@Transactional
	@Modifying
	@Query(value = "update Order o set o.state = com.school.entity.Order$State.APPROVED where o.id = :order_id")
	int changeStateToApprove(@Param("order_id") UUID id);
}

package com.school.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.school.entity.User;

@Repository
@Qualifier(value = "UserRepository")
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "select u from User u where lower(u.email) = lower(:email)")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "select u from User u where lower(u.email) = lower(:email) and u.isActive = true")
    Optional<User> findByEmailActive(@Param("email") String email);

    @Query(value = "select u from User u where u.status = 'TUTOR' and u.isActive = true")
    Page<User> findAllTutors(Pageable pageable);

    @Query(value = "select u from User u where u.status = 'TUTOR' and u.subject = :subject and u.isActive = true")
    Page<User> findAllTutorsWithSubject(@Param("subject") User.Subject subject, Pageable pageable);

    @Query(value = "select u from User u where u.status = 'TUTOR' and u.goal = :goal and u.isActive = true")
    Page<User> findAllTutorsWithGoal(@Param("goal") User.Goal goal, Pageable pageable);
    @Query(value = "select u from User u where u.status = 'TUTOR' and u.level = :level and u.isActive = true")
    Page<User> findAllTutorsWithLevel(@Param("level") User.Level level, Pageable pageable);

    @Query(value = "select u from User u where "
    		+ "(:email is null or lower(u.email) = lower(:email)) "
    		+ "and (:firstName is null or u.firstName = :firstName) "
    		+ "and (:lastName is null or u.lastName = :lastName) "
    		+ "and (:subject is null or u.subject = :subject) "
    		+ "and (:level is null or u.level = :level) "
    		+ "and (:goal is null or u.goal = :goal) "
    		+ "and (:phone is null or u.phone = :phone) "
    		+ "and (:address is null or u.address = :address) "
    		+ "and u.status = 'STUDENT' "
    		+ "and u.isActive = true")
    Page<User> findAllStudentsWithSortParam(@Param("email") String email, 
    		@Param("firstName") String firstName,
    		@Param("lastName") String lastName, 
			@Param("subject") User.Subject subject, 
			@Param("level") User.Level level,
			@Param("goal") User.Goal goal,
			@Param("phone") String phone,
			@Param("address") String address,
    		Pageable pageable);

    @Query(value = "select u from User u where u.id = :id and u.isActive = true")
    Optional<User> findByIdActive(@Param("id") UUID id);
}

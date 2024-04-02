package com.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "student_id")
    private User student;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "tutor_id")
    private User tutor;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject")
    private User.Subject subject;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;

    public enum State{
        SELECTED, APPROVED, CANCELLED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(student, order.student) && Objects.equals(tutor, order.tutor) && subject == order.subject && state == order.state && Objects.equals(startDate, order.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, tutor, subject, state, startDate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", student=" + student +
                ", tutor=" + tutor +
                ", subject=" + subject +
                ", state=" + state +
                ", startDate=" + startDate +
                '}';
    }
}

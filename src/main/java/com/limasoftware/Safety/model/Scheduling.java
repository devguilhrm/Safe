package com.limasoftware.Safety.model;

import com.limasoftware.Safety.enums.SchedulingStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity(name = "Scheduling")
@Table(name = "tb_scheduling")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Scheduling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Alinhado com SERIAL (Integer auto-increment)

    @Column(nullable = false, length = 120)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false, length = 80)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchedulingStatus status = SchedulingStatus.SCHEDULED;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "attAt", nullable = false)
    private LocalDateTime attAt = LocalDateTime.now();



    // Opcional: Atualizar o timestamp de 'attat' antes de cada update
    @PreUpdate
    protected void onUpdate() {
        attAt = LocalDateTime.now();
    }
}
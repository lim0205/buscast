package com.lim0205.buscast.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String jobName;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Short status;

    private Integer processedCount;

    @Column(length = 255)
    private String message;
}
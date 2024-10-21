package com.arturk.taskmanagerms.entity;

import jakarta.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.time.LocalDateTime;

@Entity
@RevisionEntity
@Table(name = "REVINFO")
public class RevInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVINFO_SEQ")
    @SequenceGenerator(name = "REVINFO_SEQ", sequenceName = "REVINFO_SEQ", allocationSize = 1)
    @RevisionNumber
    @Column(name = "REV")
    private long rev;

    @RevisionTimestamp
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;
}
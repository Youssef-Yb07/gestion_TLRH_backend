package com.tlrh.gestion_tlrh_backend.Entity.Archive;

import java.sql.Date;

import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import javax.persistence.GeneratedValue;
import javax.persistence.InheritanceType;

import lombok.Data;

@MappedSuperclass
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Archivage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dateArchivage;
}

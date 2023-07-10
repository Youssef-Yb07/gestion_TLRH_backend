package com.tlrh.gestion_tlrh_backend.Entity.Archive;

import java.sql.Date;



import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Archivage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dateArchivage;
}

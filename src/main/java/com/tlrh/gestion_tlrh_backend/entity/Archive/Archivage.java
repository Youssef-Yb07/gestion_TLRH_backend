package com.tlrh.gestion_tlrh_backend.entity.Archive;

import java.sql.Date;



import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Archivage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dateArchivage;
}

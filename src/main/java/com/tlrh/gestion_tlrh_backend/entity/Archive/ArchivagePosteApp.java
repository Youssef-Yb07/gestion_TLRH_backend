package com.tlrh.gestion_tlrh_backend.entity.Archive;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ArchivagePosteApp extends Archivage{
    private String posteApp;
}

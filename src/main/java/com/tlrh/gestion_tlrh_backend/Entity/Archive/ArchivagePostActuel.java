package com.tlrh.gestion_tlrh_backend.Entity.Archive;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class ArchivagePostActuel {
    private String posteActuel;
}

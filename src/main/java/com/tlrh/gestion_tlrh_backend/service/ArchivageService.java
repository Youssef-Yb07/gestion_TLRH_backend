package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Archivage;
import com.tlrh.gestion_tlrh_backend.repositories.ArchivageRepository;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ArchivageService {
    @Autowired
    private ArchivageRepository archivageRepository;
    @Autowired
    private CollaborateurRepository collaborateurRepository;
    public Map<Integer,Double> getSalary(Integer id){
        List<Archivage> archivvages=archivageRepository.getArchivageByCollaborateur_Matricule(id);
        if(archivvages.isEmpty())
            return null;
        Map<Integer,Double> map=new HashMap<>();
        Integer annee=collaborateurRepository.findById(id).get().getDate_Embauche().toLocalDate().getYear();
        for (Archivage archivage:archivvages) {
            double salaire=archivage.getSalaire();
            if(map.containsKey(annee))
                map.put(annee,(salaire+map.get(annee))/2);
            else {
                map.put(annee, salaire);
            }
            annee=archivage.getDateArchivage().toLocalDate().getYear();

        }
        return map;
    }
}

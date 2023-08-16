package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Archivage;
import com.tlrh.gestion_tlrh_backend.repositories.ArchivageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ArchivageService {
    private ArchivageRepository archivageRepository;
    public Map<Integer,Double> getSalary(Integer id){
        List<Archivage> archivvages=archivageRepository.getArchivageByCollaborateur_Matricule(id);
        Map<Integer,Double> map=new HashMap<>();
        for (Archivage archivage:archivvages) {
            double salaire=archivage.getSalaire();
            Integer annee=archivage.getDateArchivage().toLocalDate().getYear();
            if(map.containsKey(annee))
                map.put(annee,(salaire+map.get(annee))/2);
            else {
                map.put(annee, salaire);
            }
        }
        return map;
    }
}

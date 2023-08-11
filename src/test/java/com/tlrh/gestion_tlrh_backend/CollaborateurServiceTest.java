package com.tlrh.gestion_tlrh_backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.tlrh.gestion_tlrh_backend.dto.CollaborateurDto;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.service.CollaborateurService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CollaborateurServiceTest {
    @Mock
    private CollaborateurRepository collaborateurRepository;

    @InjectMocks
    private CollaborateurService collaborateurService;

    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssignCollaborateurToManager() {
        // Test data
        Integer collaborateurMatricule = 1;
        Integer managerMatricule = 2;

        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setMatricule(collaborateurMatricule);

        Collaborateur managerRH = new Collaborateur();
        managerRH.setMatricule(managerMatricule);
        // managerRH.getRoles().add(new Role(managerMatricule, "Manager RH", );  //TODO fix this

        // Mocking collaborateurRepository
        when(collaborateurRepository.findById(collaborateurMatricule))
               .thenReturn(Optional.of(collaborateur));
        when(collaborateurRepository.findById(managerMatricule))
               .thenReturn(Optional.of(managerRH));

        //CollaborateurDto resultDto = collaborateurService.assignCollaborateurToManager(collaborateurMatricule, managerMatricule);

        //Assertions.assertEquals(managerMatricule, resultDto.getManagerRH());
    }



}



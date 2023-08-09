package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.entity.Diplome;
import com.tlrh.gestion_tlrh_backend.entity.Ecole;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import com.tlrh.gestion_tlrh_backend.repositories.EcoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EcoleService {

    @Autowired
    private EcoleRepository ecoleRepository;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    public Ecole createEcole(Ecole ecole){
        //check if diplome already exist
        Optional<Ecole> existingEcole = ecoleRepository.findById(ecole.getId());
        if (existingEcole.isPresent()) {
            throw new RuntimeException("This school already exists in the database");
        }

        // save diplome in bd
        return ecoleRepository.save(ecole);
    }

    public Ecole affectEcoleToACollaborateur(int  ecoleID, int collaborateurID) {
        Optional<Collaborateur> existingCollaborateur=collaborateurRepository.findById(collaborateurID);
        Optional<Ecole> existingEcole=ecoleRepository.findById(ecoleID);
        if(!existingCollaborateur.isPresent()){
            throw new RuntimeException("Collaborateur entity doesn't exist !");
        }
        if(!existingEcole.isPresent()){
            throw new RuntimeException("Ecole entity doesn't exist !");
        }
        Ecole ecole=existingEcole.get();

        // check if school's diplomas are already affected to the collaborator
        Collaborateur collaborateur=existingCollaborateur.get();
        if (ecole.getDiplomes().stream().allMatch(diplome -> diplome.getCollaborateur()==collaborateur))
        {
            throw new RuntimeException("this school's diplomas are already affected to collaborateur");
        }

        // affect school's diplomas to the collaborator
        ecole.getDiplomes().stream().forEach(diplome -> diplome.setCollaborateur(collaborateur));

        System.out.println("diplome to collaboatteur done");

        return ecoleRepository.save(ecole);
    }


    public List<Map.Entry<String, Double>> calculerPourcentageParEcole() {

              /*
        Map.Entry<String,Double> est utilisé pour stocker une paire clé-valeur
        Dans notre cas, la clé est le nom de l'école
        Et la valeur est le pourcentage de collaborateurs qui ont étudié dans cette école
            */


            List<Collaborateur> collaborateurs = collaborateurRepository.findAll();

            // Créer un dictionnaire qui stocke le nombre de collaborateurs par école
            // La clé est le nom de l'école
            // La valeur est le nombre de collaborateurs qui ont étudié dans cette école
            Map<String, Integer> countParEcole = new HashMap<>();

            int totalCollaborateurs = collaborateurs.size();

            // Parcourir tous les collaborateurs
            for (Collaborateur collaborateur : collaborateurs) {
                List<Diplome> diplomes = collaborateur.getDiplomes();
                for (Diplome diplome : diplomes) {
                    Ecole ecole = diplome.getEcole();
                    String nomEcole = ecole.getNom();

                    // Incrémenter le compteur pour cette école
                    // Si l'école n'existe pas dans le dictionnaire, la valeur par défaut est 0
                    // Si l'école existe, on incrémente sa valeur de 1
                    countParEcole.put(nomEcole, countParEcole.getOrDefault(nomEcole, 0) + 1);
                }
            }


            // Calculer les pourcentages et les stocker dans une liste d'entrées
            List<Map.Entry<String, Double>> pourcentagesParEcole = new ArrayList<>();


            // Parcourir le dictionnaire des comptes par école
            //countParEcole.entrySet() retourne un ensemble de paires clé-valeur (Map.Entry)
            for (Map.Entry<String, Integer> entry : countParEcole.entrySet()) {
                // Calculer le pourcentage nbCollaborateursDansCetteEcole / totalCollaborateurs
                //entry.getValue() retourne la valeur qui est le nombre de collaborateurs dans cette école
                double pourcentage = (entry.getValue() / (double) totalCollaborateurs) * 100;
                //entry.getKey() retourne le nom de l'école de la paire clé-valeur (Map.Entry)
                pourcentagesParEcole.add(new AbstractMap.SimpleEntry<>(entry.getKey(), pourcentage));
            }

            return pourcentagesParEcole;
        }







    }

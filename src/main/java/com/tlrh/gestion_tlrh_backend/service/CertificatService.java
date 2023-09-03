package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Certificat;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import com.tlrh.gestion_tlrh_backend.repositories.CertificatRepository;
import com.tlrh.gestion_tlrh_backend.repositories.CollaborateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Service

public class CertificatService {


    @Autowired
    private CertificatRepository certificatRepository;

    @Autowired private CollaborateurRepository collaborateurRepository;


    // creation certificat


    @Transactional
    public Certificat createCertificat(Certificat certif) throws IllegalStateException, MessagingException {
        Optional<Certificat> existingCertificat = certificatRepository.findById(certif.getId());

        if (existingCertificat.isPresent()) {
            throw new IllegalStateException("Certificat existe déjà.");
        } else {
            Certificat certificat = new Certificat();
            certificat.setNom(certif.getNom());
            certificat.setDate(certif.getDate());


            certificatRepository.save(certificat);

            return certificat;
        }
    }
    public Collaborateur affectCertificatsToCollaborateur (int idCertificat, int idCollaborateur) {
        Optional<Certificat> optionalCertificat = certificatRepository.findById(idCertificat);
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(idCollaborateur);

        if (optionalCertificat.isPresent() && optionalCollaborateur.isPresent()) {
            Certificat certificat = optionalCertificat.get();
            Collaborateur collaborateur = optionalCollaborateur.get();

            if (!(certificat.getCollaborateurs().contains(collaborateur)&&collaborateur.getCertificats().contains(certificat))) {
                certificat.getCollaborateurs().add(collaborateur);
                certificat =certificatRepository.save(certificat);

                return collaborateur;
            }
        }
        throw new IllegalStateException("Certificat or Collaborateur not found");
    }

    public Certificat getCertificatById(Integer id) {
        Optional<Certificat> optionalCertificat = certificatRepository.findById(id);

        if (optionalCertificat.isPresent()) {
            return optionalCertificat.get();
        } else {
            throw new IllegalStateException("Certificat not found");
        }
    }

    public List<Certificat> getAllCertificats() {
        if (certificatRepository.findAll().isEmpty()){
            throw new IllegalStateException("Certificats not found");
        } else {
            return certificatRepository.findAll();
        }
    }

}
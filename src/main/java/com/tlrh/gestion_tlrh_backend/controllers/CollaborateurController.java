package com.tlrh.gestion_tlrh_backend.controllers;
import com.tlrh.gestion_tlrh_backend.dto.UpdateBy3ActorsDto;
import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import com.tlrh.gestion_tlrh_backend.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/collaborateur")
@Tag(name = "Collaborateur", description = "Gestion des collaborateurs")
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class CollaborateurController {
    @Autowired
    private CollaborateurService collaborateurService;

    @Autowired
    private ExcelService excelService;

    @PutMapping("/update/By3Actors")
    public ResponseEntity<Collaborateur> UpdateCollaborateurBy3Actors(@RequestParam Integer matricule,@RequestBody UpdateBy3ActorsDto collaborateurDto) {
        try {
            return new ResponseEntity<>(collaborateurService.updateCollaborateurBy3Actors(matricule, collaborateurDto), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while updating Collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignCollaborateurToManager")
    public ResponseEntity<Collaborateur> assignCollaborateurToManager(@RequestParam Integer collaborateurMatricule, @RequestParam Integer managerMatricule) {
        try {
            return new ResponseEntity<>(collaborateurService.assignCollaborateurToManager(collaborateurMatricule, managerMatricule), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while assigning Collaborateur to Manager");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignCollaborateursToManager")
    public ResponseEntity<List<Collaborateur>> assignCollaborateursToManager(
            @RequestParam List<Integer> collaborateurMatricules,
            @RequestParam Integer managerMatricule) {
        try {
            List<Collaborateur> updatedCollaborateurs = collaborateurService.assignCollaborateursToManager(collaborateurMatricules, managerMatricule);
            return new ResponseEntity<>(updatedCollaborateurs, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //create managerRH
    @PostMapping("/createManagerRH/{id}")
    public ResponseEntity<Collaborateur> createManagerRH(@PathVariable Integer id) {
    try {
        Collaborateur NewmanagerDto = collaborateurService.createManagerRh(id);
        return new ResponseEntity<>(NewmanagerDto, HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while creating Manager RH");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @PutMapping("/ActivateStatusManagerRH")
    public ResponseEntity<Collaborateur> ActivateStatusManagerRH(@RequestParam Integer matricule) {
    try {
        return new ResponseEntity<>(collaborateurService.ActivateStatusManagerRH(matricule), HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while updating Collaborateur");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }
    @PutMapping("/DesactivateStatusManagerRH")
    public ResponseEntity<Collaborateur> DesactivateStatusManagerRH(@RequestParam Integer matricule) {
    try {
        return new ResponseEntity<>(collaborateurService.DesactivateStatusManagerRH(matricule), HttpStatus.OK);
    } catch (Exception e) {
        System.out.println("Error while updating Collaborateur");
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping("/get/all")
    public ResponseEntity<List<Collaborateur>> GetAllCollaborateurs(){
        try {
            return new ResponseEntity<>(collaborateurService.getAllCollaborateurs(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/Collaborateur/byId/{id}")

    public ResponseEntity<Collaborateur> GetCollaborateursById(@RequestParam Integer collaborateurId){
        try {
            return new ResponseEntity<>(collaborateurService.GetCollaborateurById(collaborateurId),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/ManagerRH/Activated")
    public ResponseEntity<List<Collaborateur>> getManagerRHByStatutActivated(){
        try {
            return new ResponseEntity<>(collaborateurService.getManagerRHByStatutActivated(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/ManagerRH/Desactivated")
    public ResponseEntity<List<Collaborateur>> getManagerRHByStatutDesactivated(){
        try {
            return new ResponseEntity<>(collaborateurService.getManagerRHByStatutDisactivated(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/nonAffectedCollabs")
    public ResponseEntity<List<Collaborateur>> getNonAffectedCollabs(){
        try {
            return new ResponseEntity<>(collaborateurService.getNonAffectedCollabs(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update/ByManager")
    public ResponseEntity<Collaborateur> UpdateCollaborateurByManager(@RequestParam Integer matricule,@RequestBody Collaborateur collaborateur) {
        try {
            return new ResponseEntity<>(collaborateurService.updateCollaborateurByManager(matricule, collaborateur), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while updating Collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createCollaborateur")
    public ResponseEntity<Collaborateur> createCollaborateur(@RequestBody Collaborateur collab){
        try {
            Collaborateur collaborateur= collaborateurService.createCollaborateur(collab);
            return new ResponseEntity<>(collaborateur, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error while creating collaborateur");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/upload/collaborateursData",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Collaborateur>> uploadCollaborateursData(@RequestPart("file") MultipartFile file){
        try{
            return new ResponseEntity<>(collaborateurService.saveCollaborateursToDatabase(file),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error while uploading file");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export/collaborateursData")
    public ResponseEntity<InputStreamResource> exportCollaborateursData() {
        List<Collaborateur> collaborateurs = collaborateurService.getAllCollaborateurs();

        InputStreamResource resource = excelService.exportCollaborateursToExcel(collaborateurs);

        if (resource == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "collaborateurs.xlsx"); // Specify the file name

        // Create the ResponseEntity with the InputStreamResource, headers, and HTTP status
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/get/all/Managers")
    public ResponseEntity<List<Collaborateur>> GetAllManagers(){
        try {
            return new ResponseEntity<>(collaborateurService.getAllManagerRH(),HttpStatus.OK);
        }catch (Exception e){
         e.printStackTrace();
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/NonManagers")
    public ResponseEntity<List<Collaborateur>> GetNonManagers(){
        try {
            return new ResponseEntity<>(collaborateurService.getNonManagerRH(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/ManagerWithoutAcc")
    public ResponseEntity<List<Collaborateur>> ManagerWithoutAcc(){
        try {
            return new ResponseEntity<>(collaborateurService.getManagerWithoutAccount(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/FemaleRatio")
    public ResponseEntity<Double> FemaleRatio(){
        try {
            return new ResponseEntity<>(collaborateurService.FemaleRatio(),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/MaleRatio")
    public ResponseEntity<Double> MaleRatio() {
        try {

            return new ResponseEntity<>(collaborateurService.MaleRatio(),HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getRecruitmentEvolution")
    public ResponseEntity<Map<Integer,Integer>> getRecruitmentEvolution(){
        try {
            return new ResponseEntity<>(collaborateurService.getRecruitmentEvolution(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get/Departure/Annee")
    public ResponseEntity<Map<Integer,Integer>> getDepartureEvolution(){
        try {
            return new ResponseEntity<>(collaborateurService.DepartParAnnee(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/Arrival/Annee")
    public ResponseEntity<Map<Integer,Integer>> getArrivalEvolution(){
        try {
            return new ResponseEntity<>(collaborateurService.ArriveeParAnnee(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("get/Effectifs/Debut/Annee")
    public ResponseEntity<Map<Integer,Integer>>calculateEffectifs(){
        try {
            return new ResponseEntity<>(collaborateurService.calculateEffectifs1stJanvier(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("get/TurnOver/Annee")
    public ResponseEntity<Map<Integer,Double>>TurnOverParAnnee(){
        try {
            return new ResponseEntity<>(collaborateurService.calculateTurnoverRates(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/EvolutionPostAPP/{id}")
    public ResponseEntity<Map<Integer,List<String>>> EvolutionPostAPP(@PathVariable Integer collaborateurID){
        try {
            return new ResponseEntity<>(collaborateurService.EvolutionPostAPP(collaborateurID),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/SalaryEvolution/{id}")
    public ResponseEntity<Map<Integer,Double>> getSalaryEvolution(@PathVariable Integer id){
        try {
            return new ResponseEntity<>(collaborateurService.getSalaryEvolution(id),HttpStatus.OK);
            }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/competences/{id}")
    public ResponseEntity<Map<String,Integer>> TechnologiesParNiveau(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(collaborateurService.TechnologiesParNiveau(id), HttpStatus.OK);
            } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/salaireMoyenne/{id}")
    public ResponseEntity<Map<Date,Double>> getYearlyAverageSalary(@PathVariable Integer id){
        try {
            return new ResponseEntity<>(collaborateurService.getYearlyAverageSalary(id),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get/collaborators/associated/managerRH/{managerRHMatricule}")
    public ResponseEntity<List<Collaborateur>> getCollaboratorsAssociatedToManagerRH(@PathVariable Integer managerRHMatricule){
        try {
            return new ResponseEntity<>(collaborateurService.findCollabsAssociatedToManagerRH(managerRHMatricule),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("get/salary/{id}/{start}/{end}")
    public ResponseEntity<Map<Date,Double>> getSalaryBetween(@PathVariable Integer id,@PathVariable Date start,@PathVariable Date end){
        try {
            return new ResponseEntity<>(collaborateurService.getSalaryData(id,start,end),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("get/by/matricule")
    public ResponseEntity<Collaborateur> getCollaborateurByMatricule(@RequestParam Integer matricule){
        try {
            return new ResponseEntity<>(collaborateurService.GetCollaborateurByID(matricule),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("get/CollabWithoutCompte")
    public ResponseEntity<List<Collaborateur>> getCollaborateurWithoutCompte(){
        try {
            return new ResponseEntity<>(collaborateurService.getCollabWithoutAccount(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

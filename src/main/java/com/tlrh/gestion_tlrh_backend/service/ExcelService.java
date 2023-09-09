package com.tlrh.gestion_tlrh_backend.service;

import com.tlrh.gestion_tlrh_backend.entity.Collaborateur;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class ExcelService {
    public static boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
   public static List<Collaborateur> getCollaborateursDataFromExcel(InputStream inputStream){
        List<Collaborateur> collaborateurs = new ArrayList<>();
       try {
           XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
           XSSFSheet sheet = workbook.getSheet("collaborateurs");
           int rowIndex =0;
           for (Row row : sheet){
               if (rowIndex ==0){
                   rowIndex++;
                   continue;
               }
               Iterator<Cell> cellIterator = row.iterator();
               int cellIndex = 0;
               Collaborateur collaborateur = new Collaborateur();
               while (cellIterator.hasNext()){
                   Cell cell = cellIterator.next();

                   switch (cellIndex){
                       case 0 -> collaborateur.setNom(cell.getStringCellValue());
                       case 1 -> collaborateur.setPrenom(cell.getStringCellValue());
                       case 2 -> collaborateur.setEmail(cell.getStringCellValue());
                       case 3 -> collaborateur.setAbreviationCollaborateur(cell.getStringCellValue());
                       case 4 -> collaborateur.setAncienManagerRH(cell.getStringCellValue());
                       case 5 -> collaborateur.setSexe(cell.getStringCellValue());
                       case 6 -> collaborateur.setSite(cell.getStringCellValue());
                       case 7 -> collaborateur.setBU(cell.getStringCellValue());
                       case 8 -> {
                           java.util.Date utilDate = cell.getDateCellValue();
                           java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                           collaborateur.setDate_Embauche(sqlDate);
                       }
                       case 9 -> collaborateur.setMois_BAP(cell.getStringCellValue());
                       case 10 -> {
                            java.util.Date utilDate = cell.getDateCellValue();
                            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                            collaborateur.setDate_Depart(sqlDate);
                       }
                       case 11 -> {
                           if (cell.getCellType() == CellType.BOOLEAN) {
                               collaborateur.setAncien_Collaborateur(cell.getBooleanCellValue());
                           } else if(cell.getCellType() == CellType.STRING) {
                               // If the cell contains a string, parse it to a boolean
                               String value = cell.getStringCellValue().trim().toLowerCase();
                               collaborateur.setAncien_Collaborateur(Boolean.parseBoolean(value));
                           }
                       }
                       case 12 -> {
                           if (cell.getCellType() == CellType.BOOLEAN) {
                               collaborateur.setSeminaireIntegration(cell.getBooleanCellValue());
                           } else if (cell.getCellType() == CellType.STRING) {
                               // If the cell contains a string, parse it to a boolean
                               String value = cell.getStringCellValue().trim().toLowerCase();
                               collaborateur.setSeminaireIntegration(Boolean.parseBoolean(value));
                           }
                       }

                       case 13 ->{
                            java.util.Date utilDate = cell.getDateCellValue();
                            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                            collaborateur.setDateParticipation(sqlDate);
                       }
                       case 14 -> collaborateur.setPosteAPP(cell.getStringCellValue());
                       case 15 -> collaborateur.setPosteActuel(cell.getStringCellValue());
                       case 16 -> collaborateur.setSalaireActuel((Double) cell.getNumericCellValue());
                       default -> {
                       }
                   }
                   cellIndex++;
               }
               collaborateurs.add(collaborateur);
           }
       } catch (IOException e) {
           e.getStackTrace();
       }
       return collaborateurs;
   }

    public void addBoldItalicAndCenteredHeader(XSSFSheet sheet, int rowIndex, String... headerValues) {
        Row headerRow = sheet.createRow(rowIndex);
        XSSFCellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        XSSFFont headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true); // Set the font as bold
        headerFont.setItalic(true); // Set the font as italic
        headerStyle.setFont(headerFont); // Apply the font to the style
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // Center align the header cells

        for (int i = 0; i < headerValues.length; i++) {
            headerRow.createCell(i).setCellValue(headerValues[i]);
            headerRow.getCell(i).setCellStyle(headerStyle); // Apply the headerStyle to the cell
        }
    }

    public InputStreamResource exportCollaborateursToExcel(List<Collaborateur> collaborateurs) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("collaborateurs");

            // Create header row with bold, italic, and centered text
            addBoldItalicAndCenteredHeader((XSSFSheet) sheet, 0, "Nom", "Prénom", "Email", "AbreviationCollaborateur",
                    "AncienManagerRH", "Sexe", "Site", "BU", "Date_Embauche", "Mois_BAP", "Date_Depart",
                    "Ancien_Collaborateur", "SeminaireIntegration", "DateParticipation", "PosteAPP",
                    "PosteActuel", "SalaireActuel");

            // Create rows for each collaborateur
            for (int i = 0; i < collaborateurs.size(); i++) {
                Collaborateur collaborateur = collaborateurs.get(i);
                // Add 1 to skip the header row
                Row dataRow = sheet.createRow(i + 1);

                dataRow.createCell(0).setCellValue(collaborateur.getNom());
                dataRow.createCell(1).setCellValue(collaborateur.getPrenom());
                dataRow.createCell(2).setCellValue(collaborateur.getEmail());
                dataRow.createCell(3).setCellValue(collaborateur.getAbreviationCollaborateur());
                dataRow.createCell(4).setCellValue(collaborateur.getAncienManagerRH());
                dataRow.createCell(5).setCellValue(collaborateur.getSexe());
                dataRow.createCell(6).setCellValue(collaborateur.getSite());
                dataRow.createCell(7).setCellValue(collaborateur.getBU());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                if(collaborateur.getDate_Embauche()==null){
                 dataRow.createCell(8).setCellValue("");
                }
                else {
                    dataRow.createCell(8).setCellValue(dateFormat.format(collaborateur.getDate_Embauche()));
                }

                dataRow.createCell(9).setCellValue(collaborateur.getMois_BAP());

                if(collaborateur.getDate_Depart()==null){
                    dataRow.createCell(10).setCellValue("");
                }
                else{
                    dataRow.createCell(10).setCellValue(dateFormat.format(collaborateur.getDate_Depart()));
                }
                // Convert boolean attributes to "true" or "false"
                dataRow.createCell(11).setCellValue(collaborateur.isAncien_Collaborateur() ? "true" : "false");
                dataRow.createCell(12).setCellValue(collaborateur.isSeminaireIntegration() ? "true" : "false");

                if(collaborateur.getDateParticipation()==null){
                    dataRow.createCell(13).setCellValue("");
                }
                else {
                    dataRow.createCell(13).setCellValue(dateFormat.format(collaborateur.getDateParticipation()));
                }
                dataRow.createCell(14).setCellValue(collaborateur.getPosteAPP());
                dataRow.createCell(15).setCellValue(collaborateur.getPosteActuel());
                dataRow.createCell(16).setCellValue(collaborateur.getSalaireActuel());

            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            // Convert the ByteArrayOutputStream to an InputStreamResource
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            return new InputStreamResource(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
            return new InputStreamResource(new ByteArrayInputStream(new byte[0]));
        }
    }


}
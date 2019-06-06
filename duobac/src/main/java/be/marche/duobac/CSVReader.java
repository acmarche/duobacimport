package be.marche.duobac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;

class CSVReader {

    private DbHelper dbHelper = null;
    private String matricule;
    private String nom;
    private String prenom;
    private String codePostal;
    private int codeRue;
    private String rue;
    private String adresseNumero;
    private String adresseIndice;
    private String adresseBoite;
    private String codeRedevable;
    private String codeClass;
    private int aCharge;
    private String puce;
    private String numContainer;
    private String purDateDebut;
    private String purDateFin;
    private String codeTarif;
    private int codeCapacite;
    private int codeClef;
    private int codeDechet;
    private String query;

    void open(String csvFile, DbHelper dbHelper, int annee) {

        this.dbHelper = dbHelper;
        String line;
        String cvsSplitBy = "\\|";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] data = line.split(cvsSplitBy);

                matricule = data[0];
                nom = data[1];
                prenom = data[2];
                codePostal = data[3];
                codeRue = Utils.convertToInt(data[4]);
                rue = data[5];
                adresseNumero = data[6];
                adresseIndice = data[7];
                adresseBoite = data[8];
                codeRedevable = data[9];
                codeClass = data[10];
                aCharge = Utils.convertToInt(data[11]);
                puce = data[12];
                numContainer = data[13];
                purDateDebut = data[14];
                purDateFin = data[15];
                codeTarif = data[16];
                codeCapacite = Utils.convertToInt(data[17]);

                if (matricule.matches("[0-9]+") && matricule.length() > 2) {

                    codeClef = Utils.convertToInt(data[18]);

                    try {
                        codeDechet = Utils.convertToInt(data[19]);
                    } catch (IndexOutOfBoundsException e) {
                        codeDechet = 0;
                    }

                    System.out.println(matricule + " " + nom + " " + puce);

                    int i = 20;
                    int max = data.length;

                    int duobacId = dbHelper.duobacExist(matricule, puce);
                    if (duobacId > 0) {
                        updateDuobac(duobacId);
                    } else {
                        insertDuobac();
                    }

                    insertSituationFamilliale(annee);

                    if (data.length > 20) {
                        while (i < max) {
                            String date = data[i];
                            i = i + 1;
                            String pesee = data[i];
                            if (!date.isEmpty() && !pesee.isEmpty()) {
                                insertReleve(date, pesee);
                            }
                            i++;
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDuobac() {

        query = "INSERT INTO `duobac` (`id`, `user_id`, `rdv_matricule`, `puc_no_puce`, `pur_date_debut`, `pur_date_fin`, `pur_cod_tarification`, `puc_cod_capacite`, `pur_cod_clef`, `puc_cod_dechet`, `rdv_nom`, `rdv_prenom_1`, `loc_code_post`, `rue_code_rue`, `rue_lib_1lg`, `adr_numero`, `adr_indice`, `adr_boite`, `rdv_cod_redevable`, `rdv_cod_classe`, `puc_no_conteneur`) " +
                "VALUES (NULL, NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {

            PreparedStatement preparedStmt = dbHelper.con.prepareStatement(query);
            preparedStmt.setString(1, matricule);
            preparedStmt.setString(2, puce);
            preparedStmt.setString(3, Utils.convertDate(purDateDebut));
            preparedStmt.setString(4, Utils.convertDate(purDateFin));
            preparedStmt.setString(5, codeTarif);
            preparedStmt.setInt(6, codeCapacite);
            preparedStmt.setInt(7, codeClef);
            preparedStmt.setInt(8, codeDechet);
            preparedStmt.setString(9, nom);
            preparedStmt.setString(10, prenom);
            preparedStmt.setString(11, codePostal);
            preparedStmt.setInt(12, codeRue);
            preparedStmt.setString(13, rue);
            preparedStmt.setString(14, adresseNumero);
            preparedStmt.setString(15, adresseIndice);
            preparedStmt.setString(16, adresseBoite);
            preparedStmt.setString(17, codeRedevable);
            preparedStmt.setString(18, codeClass);
            preparedStmt.setString(19, numContainer);

            preparedStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateDuobac(int id) {
        query = "UPDATE `duobac` SET `pur_date_debut` = ?, `pur_date_fin` = ?, `pur_cod_tarification` = ?, `puc_cod_capacite` = ?, `pur_cod_clef` = ?, `puc_cod_dechet` = ?, `rdv_nom` = ?, `rdv_prenom_1` = ?, `loc_code_post` = ?, `rue_code_rue` = ?, `rue_lib_1lg` = ?, `adr_numero` = ?, `adr_indice` = ?, `adr_boite` = ?, `rdv_cod_redevable` = ?, `rdv_cod_classe` = ?, `puc_no_conteneur` = ? WHERE `id` = ?";

        try {

            PreparedStatement preparedStmt = dbHelper.con.prepareStatement(query);
            preparedStmt.setString(1, Utils.convertDate(purDateDebut));
            preparedStmt.setString(2, Utils.convertDate(purDateFin));
            preparedStmt.setString(3, codeTarif);
            preparedStmt.setInt(4, codeCapacite);
            preparedStmt.setInt(5, codeClef);
            preparedStmt.setInt(6, codeDechet);
            preparedStmt.setString(7, nom);
            preparedStmt.setString(8, prenom);
            preparedStmt.setString(9, codePostal);
            preparedStmt.setInt(10, codeRue);
            preparedStmt.setString(11, rue);
            preparedStmt.setString(12, adresseNumero);
            preparedStmt.setString(13, adresseIndice);
            preparedStmt.setString(14, adresseBoite);
            preparedStmt.setString(15, codeRedevable);
            preparedStmt.setString(16, codeClass);
            preparedStmt.setString(17, numContainer);
            preparedStmt.setInt(18, id);

            preparedStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void insertSituationFamilliale(int annee) {
        query = "INSERT INTO `situation_familiale` (`rdv_matricule`, `puc_no_puce`, `annee`, `a_charge`) VALUES (?,?,?,?);";
        try {

            PreparedStatement preparedStmt = dbHelper.con.prepareStatement(query);
            preparedStmt.setString(1, matricule);
            preparedStmt.setString(2, puce);
            preparedStmt.setInt(3, annee);
            preparedStmt.setInt(4, aCharge);
            preparedStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertReleve(String date, String poids) {
        query = "INSERT INTO pesee (`puc_no_puce`,`date_pesee`,`poids`,`a_charge`) VALUES (?,?,?,?)";
        try {

            PreparedStatement preparedStmt = dbHelper.con.prepareStatement(query);
            preparedStmt.setString(1, puce);
            preparedStmt.setString(2, Utils.convertDate(date));
            preparedStmt.setFloat(3, Utils.convertNumber(poids));
            preparedStmt.setInt(4, aCharge);

            preparedStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

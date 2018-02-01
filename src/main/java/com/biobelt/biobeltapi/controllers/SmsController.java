package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.*;
import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
import com.biobelt.biobeltapi.models.sites.Ceinture;
import com.biobelt.biobeltapi.models.sites.CeintureDao;
import com.biobelt.biobeltapi.models.sites.CeintureV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by damy on 16/04/2017.
 */

@CrossOrigin
@Controller
@RequestMapping("/sms")
public class SmsController {

    /**
     * Méthode get, permet de récupérer un sms par rapport à son id
     * @param id: id du sms à récupérer
     * @return Le sms ou null si il n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération du sms
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Sms sms = smsDao.findById(UUID.fromString(id));
        if (sms == null) return Utils.getJsonMessage(false, "Le sms n'existe pas");
        return sms;

    }

    /**
     * Méthode register, permet d'ajouter un sms
     * @param numeroTelephone: numéro de téléphone de la ceinture
     * @param dateEnvoi: date de l'envoi du message
     * @param dateReception: date de la récéption du message
     * @param contenu: contenu du message
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(
            @RequestParam String numeroTelephone,
            @RequestParam String dateEnvoi,
            @RequestParam String dateReception,
            @RequestParam String contenu
    ) throws UnsupportedEncodingException {

        // Récupération et vérification de la ceinture
        CeintureV2 ceintureEmettrice = ceintureDao.findCeintureV2ByTel(numeroTelephone.trim());
        if (ceintureEmettrice == null) return Utils.getJsonMessage(true, "La ceinture n'existe pas");


        // Création du SMS
        Sms nouveauSms = new Sms(
                ceintureEmettrice,
                contenu,
                numeroTelephone,
                LocalDateTime.parse(dateEnvoi, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(dateReception, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                false,
                false
        );

        // Dernier SMS & Intervention
        Sms dernierSms = smsDao.findLastOfCeinture(ceintureEmettrice.getId());
        InterventionV2 derniereIntervention = interventionV2Dao.findLastOfCeinture(ceintureEmettrice.getId());

        // Dernier Evenement et EtatEvenement
        Evenement dernierEvenement = null;
        EtatEvenement dernierEtatEvenement = null;
        if(dernierSms != null && (derniereIntervention == null || dernierSms.getDate().isAfter(derniereIntervention.getDate()) || dernierSms.getDate().isEqual(derniereIntervention.getDate()))) {
            dernierEvenement = dernierSms;

        } else if (derniereIntervention != null && (dernierSms == null || dernierSms.getDate().isBefore(derniereIntervention.getDate()))) {
            dernierEvenement = derniereIntervention;
        } else {
            dernierEtatEvenement = new EtatEvenement();
        }

        if(dernierEtatEvenement == null) dernierEtatEvenement = etatEvenementDao.findByEvenement(dernierEvenement);

        // Création du nouvel EtatEvenement
        EtatEvenement nouvelEtatEvenement = new EtatEvenement();
        nouvelEtatEvenement.setEvenement(nouveauSms);
        nouvelEtatEvenement.setIntervention(false);

        // Copie de l'état du dernier evenement comme base
        nouvelEtatEvenement.copy(dernierEtatEvenement);

        /* Gestion du type d'SMS */
        switch (contenu.trim()){
            case "AC Power Goes on now" :

                // Incohérence
                if (dernierSms != null && dernierSms.getContenu().equals("AC Power Goes off!")) nouvelEtatEvenement.setStatut(1);

                break;
            case "AC Power Goes off!" :

                // Incohérence
                if(!dernierEtatEvenement.isEnMarche() || dernierEtatEvenement.isDiffusionNormale() || dernierEtatEvenement.isDiffusionBoost()) nouvelEtatEvenement.setStatut(1);
                // Arrêter la diffusion
                nouvelEtatEvenement.setEnMarche(false);
                nouvelEtatEvenement.setDiffusionNormale(false);
                nouvelEtatEvenement.setDiffusionBoost(false);

                // Si une diffusion était en cours
                if(dernierEtatEvenement.isDiffusionNormale()) nouvelEtatEvenement = updateEtatEvenement(nouvelEtatEvenement, dernierEtatEvenement);

                break;

            case "CO2 open" :

                // Incohérences
                if(dernierEtatEvenement.isDiffusionNormale() || dernierEtatEvenement.isDiffusionBoost() || !dernierEtatEvenement.isEnMarche()) nouvelEtatEvenement.setStatut(1);

                if(!dernierEtatEvenement.isDiffusionNormale() && !dernierEtatEvenement.isDiffusionBoost()){
                    nouvelEtatEvenement.setDiffusionNormale(true);
                    nouvelEtatEvenement = initialiserPA(nouvelEtatEvenement);
                }

                break;
            case "CO2 close" :

                // Incohérence
                if(!dernierEtatEvenement.isDiffusionNormale()){
                    nouvelEtatEvenement.setStatut(1);
                    break;
                }

                nouvelEtatEvenement.setDiffusionNormale(false);

                if(!dernierEtatEvenement.isDiffusionBoost()) nouvelEtatEvenement = updateEtatEvenement(nouvelEtatEvenement, dernierEtatEvenement);

                break;

            case "Mode BOOST ON" :

                nouvelEtatEvenement.setDiffusionBoost(true);

                break;
            case "Mode BOOST OFF" :

                if(dernierEtatEvenement.isDiffusionBoost()){
                    nouvelEtatEvenement = updateEtatEvenement(nouvelEtatEvenement, dernierEtatEvenement);
                    nouvelEtatEvenement.setDiffusionBoost(false);
                }

                break;

            case "CO2 tank-1 empty" :

                nouvelEtatEvenement.setReserveActive(2);

                break;
            case "CO2 tank1 non-empty" :

                nouvelEtatEvenement.setReserveActive(1);

                break;

            case "CO2 tank-2 empty" :

                nouvelEtatEvenement.setReserveActive(-1);

                break;
            case "CO2 tank-2 non empty" :

                nouvelEtatEvenement.setReserveActive(2);

                break;
        }

        /* Calcul du temps restant en B1 et en B2 */
        float activiteJournaliere = getActivite7jours(ceintureEmettrice, nouvelEtatEvenement.getEvenement().getDate()) / 3600; // activitée moyenne journalière en heure
        float consommationJournaliere = activiteJournaliere * nouvelEtatEvenement.getDebitActuel();
        // B1
        int dureeB1 = Math.round((nouvelEtatEvenement.getEtatB1() > 0 ? nouvelEtatEvenement.getEtatB1() : 0) / consommationJournaliere);
        nouvelEtatEvenement.setDureeB1(dureeB1);
        // B2
        int dureeB2 = Math.round((nouvelEtatEvenement.getEtatB2() > 0 ? nouvelEtatEvenement.getEtatB2() : 0) / consommationJournaliere);
        nouvelEtatEvenement.setDureeB2(dureeB2);

        smsDao.save(nouveauSms);
        etatEvenementDao.save(nouvelEtatEvenement);

        return Utils.getJsonMessage(true, "Sms enregistré avec succès.", nouveauSms.getId());
    }


    @Autowired
    SmsDao smsDao;

    @Autowired
    CeintureDao ceintureDao;

    @Autowired
    EtatEvenementDao etatEvenementDao;

    @Autowired
    InterventionV2Dao interventionV2Dao;


    private int getActivite7jours(Ceinture ceintureEmettrice, LocalDateTime dateDepart) {
        List<Integer> activite = new ArrayList<>();

        for (int j = 2; j < 9; j++)
        {
            int sommeActivitee = 0;

            // Récupération des Sms
            List<Sms> smsPrecedents = new ArrayList<>();
            smsPrecedents.addAll(
                    smsDao.findByCeintureForActivity(
                            ceintureEmettrice.getId(),
                            dateDepart.minusDays(j).withHour(0).withMinute(0).withSecond(0).toString(),
                            dateDepart.minusDays(j).withHour(23).withMinute(59).withSecond(59).toString())
            );

            // Tri des sms
            if (smsPrecedents.size() % 2 == 0) {
                for (int i = 0; i < smsPrecedents.size(); i += 2) {
                    if (smsPrecedents.get(i).getContenu().equals("CO2 open") && smsPrecedents.get(i + 1).getContenu().equals("CO2 close")) {
                        sommeActivitee += ChronoUnit.SECONDS.between(smsPrecedents.get(i).getDateReception(), smsPrecedents.get(i + 1).getDateReception());
                    }
                }
            }

            activite.add(sommeActivitee);
        }

        // Si liste vide on retourne 0
        if (activite.size() == 0) return 0;

        // Sinon on retourne le résultat le plus grand
        activite.sort(Collections.reverseOrder());

        return activite.get(0);
    }

    private EtatEvenement updateEtatEvenement(EtatEvenement newEtatEvenement, EtatEvenement lastEtatEvenement){
        float secondesDepuisLastEvent = ChronoUnit.SECONDS.between(lastEtatEvenement.getEvenement().getDate(), newEtatEvenement.getEvenement().getDate());
        float quantiteDiffuseeDepuisLastEvent = (secondesDepuisLastEvent/3600) * lastEtatEvenement.getDebitActuel();
        if(lastEtatEvenement.isDiffusionBoost())
            quantiteDiffuseeDepuisLastEvent = quantiteDiffuseeDepuisLastEvent*2;

        newEtatEvenement.setDureeDiffusionPa(lastEtatEvenement.getDureeDiffusionPa() + secondesDepuisLastEvent);
        newEtatEvenement.setDureeDiffusionIntervention(lastEtatEvenement.getDureeDiffusionIntervention() + secondesDepuisLastEvent);
        newEtatEvenement.setDureeDiffusionTotal(lastEtatEvenement.getDureeDiffusionTotal() + secondesDepuisLastEvent);

        newEtatEvenement.setQuantiteDiffuseePa(lastEtatEvenement.getQuantiteDiffuseePa() + quantiteDiffuseeDepuisLastEvent);
        newEtatEvenement.setQuantiteDiffuseeIntervention(lastEtatEvenement.getQuantiteDiffuseeIntervention() + quantiteDiffuseeDepuisLastEvent);
        newEtatEvenement.setQuantiteDiffuseeTotal(lastEtatEvenement.getQuantiteDiffuseeTotal() + quantiteDiffuseeDepuisLastEvent);

        if(lastEtatEvenement.getReserveActive() == 1)
            newEtatEvenement.setEtatB1(lastEtatEvenement.getEtatB1() - quantiteDiffuseeDepuisLastEvent);
        else if(lastEtatEvenement.getReserveActive() == 2)
            newEtatEvenement.setEtatB2(lastEtatEvenement.getEtatB2() - quantiteDiffuseeDepuisLastEvent);

        return newEtatEvenement;
    }

    private EtatEvenement initialiserPA(EtatEvenement newEtatEvenement){
        newEtatEvenement.setDureeDiffusionPa(0f);
        newEtatEvenement.setQuantiteDiffuseePa(0f);

        return newEtatEvenement;
    }

}

package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.*;
import com.biobelt.biobeltapi.models.bouteilles.BouteilleDao;
import com.biobelt.biobeltapi.models.interventions.*;
import com.biobelt.biobeltapi.models.observation.Observation;
import com.biobelt.biobeltapi.models.observation.ObservationDao;
import com.biobelt.biobeltapi.models.observation.SupportDao;
import com.biobelt.biobeltapi.models.sites.Ceinture;
import com.biobelt.biobeltapi.models.sites.CeintureDao;
import com.biobelt.biobeltapi.models.sites.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 16/04/2017
 */

@CrossOrigin
@Controller
@RequestMapping("/intervention")
public class InterventionController {

    /**
     * Méthode getV2, permet de récupérer une interventionV2 par rapport à son id
     * @param id: id de l'interventionV2 à récupérer
     * @return L'interventionV2 ou null si elle n'existe pas
     */
    @RequestMapping(value = "/v2/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object getV2(@PathVariable("id") String id) {

        // Récupération de l'intervention
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");
        return interventionV2;

    }

    /**
     * Méthode createV2, permet de créer une interventionV2
     * @param date: date de l'interventionV2
     * @param objet: objet de l'interventionV2
     * @param operateur: opérateurs de l'interventionV2
     * @param ficheUrl: fiche de l'interventionV2
     * @param nouveauDebit: nouveauDebit de l'interventionV2
     * @param idCeinture: identifiant de la ceinture liée à l'interventionV2
     * @param initialise: initialisation de l'interventionV2
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/create", method = RequestMethod.POST)
    @ResponseBody
    public String createV2(
            @RequestParam String date,
            @RequestParam String objet,
            @RequestParam String operateur,
            @RequestParam String ficheUrl,
            @RequestParam float nouveauDebit,
            @RequestParam String idCeinture,
            @RequestParam float debitAvant,
            @RequestParam float debitApres,
            @RequestParam float pressionB1Avant,
            @RequestParam float pressionB1Apres,
            @RequestParam float pressionB2Avant,
            @RequestParam float pressionB2Apres,
            @RequestParam float pressionSortieAvant,
            @RequestParam float pressionSortieApres,
            @RequestParam boolean b1ActiveAvant,
            @RequestParam boolean b1ActiveApres,
            @RequestParam boolean initialise
    ) {

        // Récupération de la ceinture
        if (!Utils.isValidUUID(idCeinture)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Ceinture ceinture = ceintureDao.findById(UUID.fromString(idCeinture));
        if (ceinture == null) return Utils.getJsonMessage(false, "La ceinture n'existe pas");


        // Création, Vérification et Sauvegarde de l'interventionV2
        InterventionV2 interventionV2 = new InterventionV2(objet, operateur, ficheUrl, nouveauDebit, LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ceinture,
                debitAvant, debitApres, pressionB1Avant, pressionB1Apres, pressionB2Avant, pressionB2Apres, pressionSortieAvant, pressionSortieApres, b1ActiveAvant, b1ActiveApres);
        if (interventionV2.getDate().equals(interventionV2Dao.findLastOfCeinture(ceinture.getId()).getDate())) {
            return Utils.getJsonMessage(true, "L'intervention existe déjà", interventionV2.getId());
        }
        interventionV2Dao.save(interventionV2);


        // Initialisation de l'intervention
        if (initialise) {
            // Plages de diffusion
            InterventionV2 interventionV2Precedente = interventionV2Dao.findLastOfCeinture(ceinture.getId());
            if (interventionV2Precedente != null) {
                for (PlageDiffusion p : plageDiffusionDao.findByInterventionV2(interventionV2Precedente)) {
                    if (!p.isAvant()) {
                        plageDiffusionDao.save(new PlageDiffusion(p.getDebut(), p.getFin(), p.getCombinaison(), true, interventionV2));
                        plageDiffusionDao.save(new PlageDiffusion(p.getDebut(), p.getFin(), p.getCombinaison(), false, interventionV2));
                    }
                }
            }
        }


        // Damien
        // Récupération des éléments
        Sms lastSms = smsDao.findLastOfCeinture(ceinture.getId());
        InterventionV2 lastInterventionV2 = interventionV2Dao.findLastOfCeinture(ceinture.getId());
        Evenement lastEvenement = null;
        EtatEvenement lastEtatEvenement;
        if(lastSms != null && (lastInterventionV2 == null || lastSms.getDate().isAfter(lastInterventionV2.getDate()) || lastSms.getDate().isEqual(lastInterventionV2.getDate()))) {
            lastEvenement = lastSms;
        } else if (lastInterventionV2 != null && (lastSms == null || lastSms.getDate().isBefore(lastInterventionV2.getDate()))) {
            lastEvenement = lastInterventionV2;
        }
        lastEtatEvenement = etatEvenementDao.findByEvenement(lastEvenement);

        // Création de l'EtatEvenement associé à l'intervention
        EtatEvenement etatEvenement = new EtatEvenement();
        if(lastEtatEvenement != null) etatEvenement.copyWithoutEtats(lastEtatEvenement);
        etatEvenement.setEvenement(interventionV2);
        etatEvenement.setIntervention(true);
        etatEvenement.setDureeDiffusionIntervention(0.0f);
        etatEvenement.setQuantiteDiffuseeIntervention(0.0f);
        if(nouveauDebit > 0) etatEvenement.setDebitActuel(nouveauDebit);

        /* Calcul du temps restant en B1 et en B2 */
        float activiteJournaliere = getActivite7jours(ceinture, etatEvenement.getEvenement().getDate()) / 3600; // activitée moyenne journalière en heure
        float consommationJournaliere = activiteJournaliere * etatEvenement.getDebitActuel();
        // B1
        int dureeB1 = Math.round((etatEvenement.getEtatB1() > 0 ? etatEvenement.getEtatB1() : 0) / consommationJournaliere);
        etatEvenement.setDureeB1(dureeB1);
        // B2
        int dureeB2 = Math.round((etatEvenement.getEtatB2() > 0 ? etatEvenement.getEtatB2() : 0) / consommationJournaliere);
        etatEvenement.setDureeB2(dureeB2);

        etatEvenementDao.save(etatEvenement);

        return Utils.getJsonMessage(true, "Intervention ajoutée avec succès", interventionV2.getId());

    }

    /**
     * Méthode editV2, permet de modifier une interventionV2 par rapport à son id
     * @param objet: objet de l'interventionV2
     * @param operateur: opérateurs de l'interventionV2
     * @param ficheUrl: fiche de l'interventionV2
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String editV2(
            @PathVariable("id") String id,
            @RequestParam String objet,
            @RequestParam String operateur,
            @RequestParam String ficheUrl,
            @RequestParam float debitAvant,
            @RequestParam float debitApres,
            @RequestParam float pressionB1Avant,
            @RequestParam float pressionB1Apres,
            @RequestParam float pressionB2Avant,
            @RequestParam float pressionB2Apres,
            @RequestParam float pressionSortieAvant,
            @RequestParam float pressionSortieApres,
            @RequestParam boolean b1ActiveAvant,
            @RequestParam boolean b1ActiveApres
    ) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");


        // Modification & Sauvegarde
        interventionV2.setObjet(objet);
        interventionV2.setOperateur(operateur);
        interventionV2.setFicheUrl(ficheUrl);
        interventionV2.setDebitAvant(debitAvant);
        interventionV2.setDebitApres(debitApres);
        interventionV2.setPressionB1Avant(pressionB1Avant);
        interventionV2.setPressionB1Apres(pressionB1Apres);
        interventionV2.setPressionB2Avant(pressionB2Avant);
        interventionV2.setPressionB2Apres(pressionB2Apres);
        interventionV2.setPressionSortieAvant(pressionSortieAvant);
        interventionV2.setPressionSortieApres(pressionSortieApres);
        interventionV2.setB1ActiveAvant(b1ActiveAvant);
        interventionV2.setB1ActiveApres(b1ActiveApres);
        interventionV2Dao.save(interventionV2);
        return Utils.getJsonMessage(true, "Intervention modifiée avec succès", interventionV2.getId());

    }

    /**
     * Méthode deleteV2, permet de supprimer une interventionV2 par rapport à son id
     * @param id: id de la bouteille à supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteV2(@PathVariable("id") String id) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'interventionV2 n'existe pas");


        // Suppression
        for (Observation o : observationDao.findByInterventionV2(interventionV2)) supportDao.delete(supportDao.findByObservation(o));
        observationDao.delete(observationDao.findByInterventionV2(interventionV2));
        plageDiffusionDao.delete(plageDiffusionDao.findByInterventionV2(interventionV2));
        debitPiegeDao.delete(debitPiegeDao.findByInterventionV2(interventionV2));
        bouteilleDao.delete(bouteilleDao.findByInterventionV2(interventionV2));
        interventionV2Dao.delete(interventionV2);
        return Utils.getJsonMessage(true, "InterventionV2 supprimée avec succès", null);

    }


    /**
     * Méthode getBouteillesV2, permet de récupérer toutes les bouteilles d'une interventionV2 par rapport à son id
     * @param id: id de l'interventionV2
     * @return Les bouteilles de l'interventionV2
     */
    @RequestMapping(value = "/v2/{id}/bouteilles", method = RequestMethod.GET)
    @ResponseBody
    public Object getBouteillesV2(@PathVariable("id") String id) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");


        // Récupération des bouteilles
        return bouteilleDao.findByInterventionV2(interventionV2);

    }

    /**
     * Méthode getPlagesDiffusionV2, permet de récupérer les plages de diffusion d'une interventionV2 par rapport à son id
     * @param id: id de l'interventionV2
     * @return Les plages de diffusion de l'interventionV2
     */
    @RequestMapping(value = "/v2/{id}/plagesdiffusion", method = RequestMethod.GET)
    @ResponseBody
    public Object getPlagesDiffusionV2(@PathVariable("id") String id) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");

        // Récupération des plages de diffusion
        return plageDiffusionDao.findByInterventionV2(interventionV2);

    }

    /**
     * Méthode getObservationsV2, permet de récupérer toutes les observations d'une interventionV2 par rapport à son id
     * @param id: id de l'interventionV2
     * @return Les observations de l'interventionV2
     */
    @RequestMapping(value = "/v2/{id}/observations", method = RequestMethod.GET)
    @ResponseBody
    public Object getObservationsV2(@PathVariable("id") String id) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");


        // Récupération des observations
        return observationDao.findByInterventionV2(interventionV2);

    }

    /**
     * Méthode getDebitPiegesV2, permet de récupérer les débits pièges d'une interventionV2 par rapport à son id
     * @param id: id de l'interventionV2
     * @return Les débits pièges de l'interventionV2
     */
    @RequestMapping(value = "/v2/{id}/debitpieges", method = RequestMethod.GET)
    @ResponseBody
    public Object getDebitPiegesV2(@PathVariable("id") String id) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(id));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");

        // Récupération des débits pièges
        return debitPiegeDao.findByInterventionV2(interventionV2);

    }


    @Autowired
    InterventionV2Dao interventionV2Dao;

    @Autowired
    CeintureDao ceintureDao;

    @Autowired
    PlageDiffusionDao plageDiffusionDao;

    @Autowired
    ObservationDao observationDao;

    @Autowired
    DebitPiegeDao debitPiegeDao;

    @Autowired
    EtatEvenementDao etatEvenementDao;

    @Autowired
    BouteilleDao bouteilleDao;

    @Autowired
    SiteDao siteDao;

    @Autowired
    SupportDao supportDao;

    @Autowired
    SmsDao smsDao;


    private int getActivite7jours(Ceinture ceintureEmettrice, LocalDateTime dateDepart) {
        List<Integer> activite = new ArrayList<>();

        for (int j = 2; j < 9; j++)
        {
            int sommeActivitee = 0;
            List<Sms> smsPrecedents = new ArrayList<>();
            // Date
            LocalDateTime dateAvant = dateDepart.minusDays(j).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime dateApres = dateDepart.minusDays(j).withHour(23).withMinute(59).withSecond(59);

            // Tri des sms
            for (Sms s : smsDao.findAll()) {
                if (ceintureEmettrice.getId() == s.getCeinture().getId() && s.getDateReception().isBefore(dateApres) && s.getDateReception().isAfter(dateAvant) && (s.getContenu().equals("CO2 open") || s.getContenu().equals("CO2 close"))) {
                    smsPrecedents.add(s);
                }
            }
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
}

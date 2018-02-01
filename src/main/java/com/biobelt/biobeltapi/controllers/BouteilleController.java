package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.*;
import com.biobelt.biobeltapi.models.bouteilles.Bouteille;
import com.biobelt.biobeltapi.models.bouteilles.BouteilleDao;
import com.biobelt.biobeltapi.models.bouteilles.TypeBouteille;
import com.biobelt.biobeltapi.models.bouteilles.TypeBouteilleDao;
import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
import com.biobelt.biobeltapi.models.sites.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 29/06/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/bouteille")
public class BouteilleController {

    /**
     * Méthode get, permet de récupérer une bouteille par rapport à son id
     * @param id: id de la bouteille à récupérer
     * @return La bouteille ou null si elle n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération de la bouteille
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Bouteille bouteille = bouteilleDao.findById(UUID.fromString(id));
        if (bouteille == null) return Utils.getJsonMessage(false, "La bouteille n'existe pas");
        return bouteille;

    }


    /**
     * Méthode createV2, permet de créer une bouteille
     * @param etat: état de la bouteille (NEUVE / UTILISE)
     * @param b1: La bouteille est-elle en b1 ou en b2
     * @param remplissage: remplissage de la bouteille (en %)
     * @param idTypeBouteille: identifiant du type de bouteille lié à la bouteille
     * @param idInterventionV2: identifiant de l'interventionV2 liée à la bouteille
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/create", method = RequestMethod.POST)
    @ResponseBody
    public String createV2(
            @RequestParam(defaultValue = "NEUVE", required = false) String etat,
            @RequestParam(defaultValue = "true", required = false) Boolean b1,
            @RequestParam(defaultValue = "100", required = false) Float remplissage,
            @RequestParam(defaultValue = "", required = false) String idTypeBouteille,
            @RequestParam(defaultValue = "", required = false) String idInterventionV2
    ) {

        // Récupération du type de bouteille
        if (!Utils.isValidUUID(idTypeBouteille)) return Utils.getJsonMessage(false, "Identifiant invalide");
        TypeBouteille typeBouteille = typeBouteilleDao.findById(UUID.fromString(idTypeBouteille));
        if (typeBouteille == null) return Utils.getJsonMessage(false, "Le type de bouteille n'existe pas");


        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(idInterventionV2)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(idInterventionV2));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");


        // Création de la bouteilles
        Bouteille bouteille = new Bouteille(typeBouteille, remplissage, b1, Bouteille.Etat.valueOf(etat), interventionV2);


        /// Damien
        // Mise à jour de l'état de la ceinture
        EtatEvenement etatEvenementIntervention = etatEvenementDao.findByEvenement(interventionV2);
        if(bouteille.getEtat() == Bouteille.Etat.NEUVE) {
            // Bouteille Neuve
            if (b1) {
                etatEvenementIntervention.setEtatB1(etatEvenementIntervention.getEtatB1() +
                        ((typeBouteille.getDesignation() * bouteille.getRemplissage()) / 100));
            } else {
                etatEvenementIntervention.setEtatB2(etatEvenementIntervention.getEtatB2() +
                        ((typeBouteille.getDesignation() * bouteille.getRemplissage()) / 100));
            }
        } else {
            // Bouteille Usée
            List<Bouteille> bouteilleList = bouteilleDao.findByInterventionV2(interventionV2);

            Boolean alreadyHaveUtiliseeBouteilleB1 = false;
            Boolean alreadyHaveUtiliseeBouteilleB2 = false;
            for (Bouteille b: bouteilleList) {
                if(Bouteille.Etat.UTILISE == b.getEtat()){
                    if(b.isB1())
                        alreadyHaveUtiliseeBouteilleB1 = true;
                    else
                        alreadyHaveUtiliseeBouteilleB2 = true;
                }
            }

            Sms lastSms = smsDao.findLastOfCeinture(interventionV2.getCeinture().getId());

            InterventionV2 lastIntervention = interventionV2Dao.findSecondeToLastOfCeinture(interventionV2.getCeinture().getId());

            Evenement lastEvenement = null;
            EtatEvenement lastEtatEvenement;

            if(lastSms!=null && (lastIntervention==null || lastSms.getDate().isAfter(lastIntervention.getDate()) || lastSms.getDate().isEqual(lastIntervention.getDate()) ))
                lastEvenement = lastSms;
            else if (lastIntervention!=null && (lastSms==null || lastSms.getDate().isBefore(lastIntervention.getDate()) ))
                lastEvenement = lastIntervention;

            lastEtatEvenement = etatEvenementDao.findByEvenement(lastEvenement);

            if(!alreadyHaveUtiliseeBouteilleB1 && bouteille.isB1()){
                //ajouter ancien etats b1
                etatEvenementIntervention.setEtatB1(etatEvenementIntervention.getEtatB1() + lastEtatEvenement.getEtatB1());
            }
            if(!alreadyHaveUtiliseeBouteilleB2 && !bouteille.isB1()){
                //ajouter ancien etats b2
                etatEvenementIntervention.setEtatB2(etatEvenementIntervention.getEtatB2() + lastEtatEvenement.getEtatB2());
            }
        }
        etatEvenementDao.save(etatEvenementIntervention);


        // Sauvegarde de la bouteille
        bouteilleDao.save(bouteille);
        return Utils.getJsonMessage(true, "Bouteille ajoutée avec succès", bouteille.getId());

    }


    /**
     * Méthode remove, permet de supprimer une bouteille par rapport à son id
     * @param id: id de la bouteille à supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String remove(@PathVariable("id") String id) {

        // Récupération de la bouteille
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Bouteille bouteille = bouteilleDao.findById(UUID.fromString(id));
        if (bouteille == null) return Utils.getJsonMessage(false, "La bouteille n'existe pas");


        // Suppression de la bouteilles
        bouteilleDao.delete(bouteille);
        return Utils.getJsonMessage(true, "La bouteille a bien été supprimée", null);
    }


    @Autowired
    BouteilleDao bouteilleDao;


    @Autowired
    TypeBouteilleDao typeBouteilleDao;


    @Autowired
    InterventionV2Dao interventionV2Dao;

    @Autowired
    EtatEvenementDao etatEvenementDao;

    @Autowired
    SmsDao smsDao;


    @Autowired
    SiteDao siteDao;

}

package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.Sms;
import com.biobelt.biobeltapi.models.SmsDao;
import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
import com.biobelt.biobeltapi.models.observation.Observation;
import com.biobelt.biobeltapi.models.observation.ObservationDao;
import com.biobelt.biobeltapi.models.observation.SupportDao;
import com.biobelt.biobeltapi.models.sites.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 16/04/2017
 */

@CrossOrigin
@Controller
@RequestMapping("/ceinture")
public class CeintureController {

    /**
     * Méthode get, permet de récupérer une ceinture par rapport à son id
     * @param id: id de la ceinture à récupérer
     * @return La ceinture ou null si elle n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération de la ceinture
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Ceinture ceinture = ceintureDao.findById(UUID.fromString(id));
        if (ceinture == null) return Utils.getJsonMessage(false, "La ceinture n'existe pas");
        return ceinture;

    }

    /**
     * Méthode createV2, permet de créer une ceintureV2
     * @param nom: nom de la ceinture
     * @param numeroTelephone: numero de telephone de la ceinture
     * @param upc: numero de série de la ceinture
     * @param nombrePieges: nombre de pièges de la ceinture
     * @param inversionEmpty: la ceinture est en inversionEmpty
     * @param commentaire: commentaire de la ceinture
     * @param hivernage: hivernage de la ceinture
     * @param idSite: identifiant du site associé à la ceinture
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/create", method = RequestMethod.POST)
    @ResponseBody
    public String createV2(
            @RequestParam String nom,
            @RequestParam String numeroTelephone,
            @RequestParam String upc,
            @RequestParam Integer nombrePieges,
            @RequestParam boolean inversionEmpty,
            @RequestParam String commentaire,
            @RequestParam boolean hivernage,
            @RequestParam String idSite
    ) {

        // On vérifie qu'une ceinture non archivée n'existe pas déjà
        for (Ceinture c : ceintureDao.findAll()) {
            if (!c.isArchive() && c instanceof CeintureV2) {
                CeintureV2 ceinture = (CeintureV2) c;
                if (ceinture.getNom().equals(nom)) {
                    return Utils.getJsonMessage(false, "Une ceinture portant le même nom existe déjà", c.getId());
                } else if (ceinture.getNumeroTelephone().equals(numeroTelephone)) {
                    return Utils.getJsonMessage(false, "Une ceinture possédant le même numéro de téléphone existe déjà", c.getId());
                }
            }
        }


        // Récupération du site
        if (!Utils.isValidUUID(idSite)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(idSite));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");


        // Création & Sauvegarde
        CeintureV2 ceintureV2 = new CeintureV2(nom, upc, nombrePieges, commentaire, hivernage, site, numeroTelephone, inversionEmpty);
        ceintureDao.save(ceintureV2);
        return Utils.getJsonMessage(true, "Ceinture créée avec succès", ceintureV2.getId());

    }

    /**
     * Méthode editV2, permet de modifier une ceintureV2 par rapport à son id
     * @param id: id de la ceintureV2 à supprimer
     * @param nom: nom de la ceinture
     * @param numeroTelephone: numero de telephone de la ceinture
     * @param upc: numero de série de la ceinture
     * @param nombrePieges: nombre de pièges de la ceinture
     * @param inversionEmpty: la ceinture est en inversionEmpty
     * @param commentaire: commentaire de la ceinture
     * @param hivernage: hivernage de la ceinture
     * @param idSite: identifiant du site associé à la ceinture
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String editV2(
            @PathVariable("id") String id,
            @RequestParam String nom,
            @RequestParam String numeroTelephone,
            @RequestParam String upc,
            @RequestParam String nombrePieges,
            @RequestParam String inversionEmpty,
            @RequestParam String commentaire,
            @RequestParam String hivernage,
            @RequestParam String archive,
            @RequestParam String idSite
    ) {

        // Récupération & Vérification de la ceinture
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Ceinture ceinture = ceintureDao.findById(UUID.fromString(id));
        if (ceinture == null || !(ceinture instanceof CeintureV2)) return Utils.getJsonMessage(false, "La ceinture n'existe pas");
        CeintureV2 ceintureV2 = (CeintureV2) ceinture;

        // Modification & Sauvegarde
        ceintureV2.setNom(nom);
        ceintureV2.setNumeroTelephone(numeroTelephone);
        ceintureV2.setUpc(upc);
        ceintureV2.setNombrePieges(Integer.parseInt(nombrePieges));
        ceintureV2.setInversionEmpty(Boolean.parseBoolean(inversionEmpty));
        ceintureV2.setCommentaire(commentaire);
        ceintureV2.setHivernage(Boolean.parseBoolean(hivernage));
        ceintureV2.setArchive(Boolean.parseBoolean(archive));
        if (idSite.equals("0")) ceintureV2.setSite(null);
        else if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        else ceintureV2.setSite(siteDao.findById(UUID.fromString(id)));
        ceintureDao.save(ceintureV2);
        return Utils.getJsonMessage(true, "Ceinture modifiée avec succès", ceinture.getId());

    }

    /**
     * Méthode getInterventionsV2, permet de récupérer toutes les interventions d'une ceinture par rapport à son id
     * @param id: id de la ceinture
     * @return Les interventions de la ceinture
     */
    @RequestMapping(value = "/v2/{id}/interventions", method = RequestMethod.GET)
    @ResponseBody
    public Object getInterventionsV2(@PathVariable("id") String id) {

        // Récupération de la ceinture
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Ceinture ceinture = ceintureDao.findById(UUID.fromString(id));
        if (ceinture == null || !(ceinture instanceof CeintureV2)) return Utils.getJsonMessage(false, "La ceinture n'existe pas");

        // Récupération des interventions
        return interventionV2Dao.findByCeinture(ceinture);

    }


    @Autowired
    CeintureDao ceintureDao;

    @Autowired
    SiteDao siteDao;

    @Autowired
    InterventionV2Dao interventionV2Dao;

    @Autowired
    ObservationDao observationDao;

    @Autowired
    SupportDao supportDao;

    @Autowired
    SmsDao smsDao;

}

package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.interventions.DebitPiege;
import com.biobelt.biobeltapi.models.interventions.DebitPiegeDao;
import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
import com.biobelt.biobeltapi.models.sites.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.0
 * @since 23/06/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/debitpiege")
public class DebitPiegeController {

    /**
     * Méthode get, permet de récupérer un debitPiege par rapport à son id
     * @param id: id du debitPiege à récupérer
     * @return Le debitPiege ou propage TypeBouteilleInexistantException si il n'existe pas
     */
    public Object get(@PathVariable("id") String id) {

        // Récupération du debitPiege
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        DebitPiege debitPiege = debitPiegeDao.findById(UUID.fromString(id));
        if (debitPiege == null) return Utils.getJsonMessage(false, "Le debitPiege n'existe pas");
        return debitPiege;

    }

    /**
     * Méthode createV2, permet de créer un debitPiege
     * @param debit: relevé du débit module-piège (en l/min)
     * @param numeroPiege: numéro du piège
     * @param idInterventionV2: identifiant de l'interventionV2 liée au debitPiege
     * @return Le résultat de l'execution de la commande
     */
    @RequestMapping(value = "/v2/create", method = RequestMethod.POST)
    @ResponseBody
    public String createV2(
            @RequestParam float debit,
            @RequestParam Integer numeroPiege,
            @RequestParam String idInterventionV2
    ) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(idInterventionV2)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(idInterventionV2));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");


        // Création & Sauvegarde
        DebitPiege debitPiege = new DebitPiege(debit, numeroPiege, interventionV2);
        debitPiegeDao.save(debitPiege);
        return Utils.getJsonMessage(true, "Débit piège ajouté avec succès", debitPiege.getId());

    }

    /**
     * Méthode edit, permet de modifier un debitPiege par rapport à son id
     * @param id: id du debitPiege à modifier
     * @param debit: relevé du débit module-piège (en l/min)
     * @param numeroPiege: numéro du piège
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(
            @PathVariable("id") String id,
            @RequestParam String debit,
            @RequestParam String numeroPiege
    ) {

        // Récupération du debitPiege
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        DebitPiege debitPiege = debitPiegeDao.findById(UUID.fromString(id));
        if (debitPiege == null) return Utils.getJsonMessage(false, "Le debitPiege n'existe pas");


        // Modification & Sauvegarde
        debitPiege.setDebit(Float.parseFloat(debit));
        debitPiege.setNumeroPiege(Integer.parseInt(numeroPiege));
        debitPiegeDao.save(debitPiege);
        return Utils.getJsonMessage(true, "Débit piège modifié avec succès", debitPiege.getId());

    }

    /**
     * Méthode delete, permet de supprimer un debitPiege par rapport à son id
     * @param id: id du debitPiege à supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération du debitPiege
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        DebitPiege debitPiege = debitPiegeDao.findById(UUID.fromString(id));
        if (debitPiege == null) return Utils.getJsonMessage(false, "Le debitPiege n'existe pas");


        // Suppression
        debitPiegeDao.delete(debitPiege);
        return Utils.getJsonMessage(true, "Débit piège supprimé avec succès", null);

    }


    @Autowired
    DebitPiegeDao debitPiegeDao;

    @Autowired
    InterventionV2Dao interventionV2Dao;

    @Autowired
    SiteDao siteDao;

}

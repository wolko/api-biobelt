package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.observation.Observation;
import com.biobelt.biobeltapi.models.observation.ObservationDao;
import com.biobelt.biobeltapi.models.observation.Support;
import com.biobelt.biobeltapi.models.observation.SupportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 29/06/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/support")
public class SupportController {

    /**
     * Méthode get, permet de récupérer un support par rapport à son id
     * @param id: id du support à récupérer
     * @return Le support
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération du support
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Support support = supportDao.findById(UUID.fromString(id));
        if (support == null) return Utils.getJsonMessage(false, "Le support n'existe pas");
        return support;

    }


    /**
     * Méthode create, permet de créer un support
     * @param url: lien du support
     * @param type: type du support (IMAGE / SON / VIDEO)
     * @param idObservation: identifiant de l'observation liée au support
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(
            @RequestParam String url,
            @RequestParam String type,
            @RequestParam String idObservation
    ) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(idObservation)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(idObservation));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");


        // Création & sauvegarde
        Support support = new Support(url, Support.Type.valueOf(type), observation);
        supportDao.save(support);
        return Utils.getJsonMessage(true, "Support ajouté avec succès", support.getId());

    }


    /**
     * Méthode delete, permet de supprimer un support par rapport à son id
     * @param id: id du support à supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération du support
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Support support = supportDao.findById(UUID.fromString(id));
        if (support == null) return Utils.getJsonMessage(false, "Le support n'existe pas");


        // Suppression du support
        supportDao.delete(support);
        return Utils.getJsonMessage(true, "Support supprimé avec succès", null);

    }


    @Autowired
    SupportDao supportDao;

    @Autowired
    ObservationDao observationDao;

}

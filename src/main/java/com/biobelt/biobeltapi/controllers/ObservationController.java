package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
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
 * @since 21/06/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/observation")
public class ObservationController {

    /**
     * Méthode get, permet de récupérer une observation par rapport à son id
     * @param id: id de l'observation à récupérer
     * @return L'observation
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(id));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");
        return observation;

    }

    /**
     * Méthode createV2, permet de créer une observation
     * @param objet: objet de l'observation
     * @param description: description de l'observation
     * @param idInterventionV2: identifiant de l'intervention liée a l'observation
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/create", method = RequestMethod.POST)
    @ResponseBody
    public String createV2(
            @RequestParam String objet,
            @RequestParam String description,
            @RequestParam String idInterventionV2
    ) {

        // Récupération de l'intervention
        if (!Utils.isValidUUID(idInterventionV2)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(idInterventionV2));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'intervention n'existe pas");


        // Création & Sauvegarde
        Observation observation = new Observation(objet, description, interventionV2);
        observationDao.save(observation);
        return Utils.getJsonMessage(true, "Observation ajoutée avec succès", observation.getId());

    }

    /**
     * Méthode edit, permet de modifier une observation par rapport à son id
     * @param id: id de l'observation à modifier
     * @param objet: objet de l'observation
     * @param description: description de l'observation
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(
            @PathVariable("id") String id,
            @RequestParam String objet,
            @RequestParam String description
    ) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(id));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");


        // Modification & Sauvegarde
        observation.setObjet(objet);
        observation.setDescription(description);
        observationDao.save(observation);
        return Utils.getJsonMessage(true, "Observation modifiée avec succès", observation.getId());

    }

    /**
     * Méthode delete, permet de supprimer une observation par rapport à son id
     * @param id: id de l'observation à supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(id));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");


        // Suppression de l'observation
        supportDao.delete(supportDao.findByObservation(observation));
        observationDao.delete(observation);
        return Utils.getJsonMessage(true, "Observation supprimée avec succès", null);

    }


    /**
     * Méthode getImages, permet de récupérer les images d'une observation par rapport à son id
     * @param id: id de l'observation
     * @return Les images ou une liste vide si elles n'existent pas
     */
    @RequestMapping(value = "/{id}/images", method = RequestMethod.GET)
    @ResponseBody
    public Object getImages(@PathVariable("id") String id) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(id));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");


        // Récupération des images
        return supportDao.findByTypeAndObservation(Support.Type.IMAGE, observation);

    }


    /**
     * Méthode getSons, permet de récupérer les sons d'une observation par rapport à son id
     * @param id: id de l'observation
     * @return Les sons ou une liste vide s'ils n'existent pas
     */
    @RequestMapping(value = "/{id}/sons", method = RequestMethod.GET)
    @ResponseBody
    public Object getSons(@PathVariable("id") String id) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(id));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");


        // Récupération des images
        return supportDao.findByTypeAndObservation(Support.Type.SON, observation);

    }


    /**
     * Méthode getVideos, permet de récupérer les vidéos d'une observation par rapport à son id
     * @param id: id de l'observation
     * @return Les vidéos ou une liste vide si elles n'existent pas
     */
    @RequestMapping(value = "/{id}/videos", method = RequestMethod.GET)
    @ResponseBody
    public Object getVideos(@PathVariable("id") String id) {

        // Récupération de l'observation
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Observation observation = observationDao.findById(UUID.fromString(id));
        if (observation == null) return Utils.getJsonMessage(false, "L'observation n'existe pas");


        // Récupération des images
        return supportDao.findByTypeAndObservation(Support.Type.VIDEO, observation);

    }


    @Autowired
    ObservationDao observationDao;

    @Autowired
    InterventionV2Dao interventionV2Dao;

    @Autowired
    SupportDao supportDao;
}

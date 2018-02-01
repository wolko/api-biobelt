package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.interventions.InterventionV2;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
import com.biobelt.biobeltapi.models.interventions.PlageDiffusion;
import com.biobelt.biobeltapi.models.interventions.PlageDiffusionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 22/06/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/plagediffusion")
public class PlageDiffusionController {

    /**
     * Méthode get, permet de récupérer une plage de diffusion par rapport à son id
     * @param id: id de la plage de diffusion à récupérer
     * @return L'etatCo2 ou null si il n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération de la plage
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        PlageDiffusion plageDiffusion = plageDiffusionDao.findById(UUID.fromString(id));
        if (plageDiffusion == null) return Utils.getJsonMessage(false, "La plage n'existe pas");
        return plageDiffusion;

    }

    /**
     * Méthode create, permet de créer une plage de diffusion
     * @param debut: début de la plage de diffusion
     * @param fin: fin de la plage de diffusion
     * @param combinaison: combinaison de la plage de diffusion
     * @param avant: avant / après l'intervention
     * @param idInterventionV2: identifiant de l'interventionV2
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/v2/create", method = RequestMethod.POST)
    @ResponseBody
    public String createV2(
            @RequestParam String debut,
            @RequestParam String fin,
            @RequestParam Integer combinaison,
            @RequestParam Boolean avant,
            @RequestParam String idInterventionV2
    ) {

        // Récupération de l'interventionV2
        if (!Utils.isValidUUID(idInterventionV2)) return Utils.getJsonMessage(false, "Identifiant invalide");
        InterventionV2 interventionV2 = interventionV2Dao.findById(UUID.fromString(idInterventionV2));
        if (interventionV2 == null) return Utils.getJsonMessage(false, "L'interventionV2 n'existe pas");


        // Création & Sauvegarde
        PlageDiffusion plageDiffusion = new PlageDiffusion(Time.valueOf(debut), Time.valueOf(fin), combinaison, avant, interventionV2);
        plageDiffusionDao.save(plageDiffusion);
        return Utils.getJsonMessage(true, "Plage de diffusion ajoutée avec succès", plageDiffusion.getId());

    }

    /**
     * Méthode delete, permet de supprimer une plage de diffusion par rapport à son id
     * @param id: id de la plage de diffusion à modifier / supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération de la plage
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        PlageDiffusion plageDiffusion = plageDiffusionDao.findById(UUID.fromString(id));
        if (plageDiffusion == null) return Utils.getJsonMessage(false, "La plage n'existe pas");


        // Suppression
        plageDiffusionDao.delete(plageDiffusion);
        return Utils.getJsonMessage(true, "Plage de diffusion supprimée avec succès", null);

    }


    @Autowired
    PlageDiffusionDao plageDiffusionDao;

    @Autowired
    InterventionV2Dao interventionV2Dao;

}

package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.EtatEvenement;
import com.biobelt.biobeltapi.models.EtatEvenementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author damy
 * @version 1.0.1
 * @since 19/06/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/etatevenement")
public class EtatEvenementController {

    /**
     * Méthode get, permet de récupérer un etatEvenement par rapport à son id
     * @param id: id de l'etatEvenement à récupérer
     * @return L'etatEvenement ou null si il n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération de l'etatEvenement
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        EtatEvenement etatEvenement = etatEvenementDao.findById(UUID.fromString(id));
        if (etatEvenement == null) return Utils.getJsonMessage(false, "L'etatEvenement n'existe pas");
        return etatEvenement;

    }


    @Autowired
    EtatEvenementDao etatEvenementDao;

}

package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.operateurs.Operateur;
import com.biobelt.biobeltapi.models.operateurs.OperateurDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @since 20/06/2017
 * @version 1.0.0
 */

@CrossOrigin()
@Controller
@RequestMapping("/operateur")
public class OperateurController {

    /**
     * Méthode getAll, permet de récupérer tous les opérateurs
     * @return La liste de tous les opérateurs
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Operateur> getAll() {
        return operateurDao.findAll();
    }


    /**
     * Méthode get, permet de récupérer un operateurs par rapport à son id
     * @param id: id de l'opérateur à récupérer
     * @return L'opérateur ou null si il n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération de l'opérateur
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Operateur operateur = operateurDao.findById(UUID.fromString(id));
        if (operateur == null) return Utils.getJsonMessage(false, "L'opérateur n'existe pas");
        return operateur;

    }

    /**
     * Méthode create, permet de créer un opérateur
     * @param nom: nom de l'opérateur
     * @param prenom: nom de l'opérateur
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(
            @RequestParam String nom,
            @RequestParam String prenom
    ) {

        // Création & Sauvegarde
        Operateur operateur = new Operateur(nom, prenom);
        operateurDao.save(operateur);
        return Utils.getJsonMessage(true, "Opérateur ajouté avec succès", operateur.getId());

    }

    /**
     * Méthode edit, permet de modifier un opérateur par rapport à son id
     * @param id: id de l'opérateur à modifier / supprimer
     * @param nom: nom de l'opérateur
     * @param prenom: nom de l'opérateur
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String editOrRemove(
            @PathVariable("id") String id,
            @RequestParam String nom,
            @RequestParam String prenom
    ) {

        // Récupération de l'opérateur
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Operateur operateur = operateurDao.findById(UUID.fromString(id));
        if (operateur == null) return Utils.getJsonMessage(false, "L'opérateur n'existe pas");


        // Modification & Sauvegarde
        operateur.setNom(nom);
        operateur.setPrenom(prenom);
        operateurDao.save(operateur);
        return Utils.getJsonMessage(true, "Opérateur modifié avec succès", operateur.getId());

    }

    /**
     * Méthode delete, permet de supprimer un opérateur par rapport à son id
     * @param id: id de l'opérateur à supprimer
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération de l'opérateur
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Operateur operateur = operateurDao.findById(UUID.fromString(id));
        if (operateur == null) return Utils.getJsonMessage(false, "L'opérateur n'existe pas");


        // Suppression
        operateurDao.delete(operateur);
        return Utils.getJsonMessage(true, "Opérateur supprimé avec succès", null);

    }


    @Autowired
    OperateurDao operateurDao;

}

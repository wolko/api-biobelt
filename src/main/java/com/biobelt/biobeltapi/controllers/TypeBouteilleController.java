package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.bouteilles.BouteilleDao;
import com.biobelt.biobeltapi.models.bouteilles.TypeBouteille;
import com.biobelt.biobeltapi.models.bouteilles.TypeBouteilleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @since 16/04/2017
 * @version 1.0.1
 */

@CrossOrigin()
@Controller
@RequestMapping("/typebouteille")
public class TypeBouteilleController {

    /**
     * Méthode getAll, permet de récupérer tous les types
     * @return La liste de tous les types
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<TypeBouteille> getAll() {
        return typeBouteilleDao.findAll();
    }

    /**
     * Méthode get, permet de récupérer un type de bouteilles par rapport à son id
     * @param id: id du type de bouteilles à récupérer
     * @return Le type de bouteilles ou propage TypeBouteilleInexistantException si il n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération du type de bouteille
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        TypeBouteille typeBouteille = typeBouteilleDao.findById(UUID.fromString(id));
        if (typeBouteille == null) return Utils.getJsonMessage(false, "Le type de bouteille n'existe pas");
        return typeBouteille;

    }


    /**
     * Méthode create, permet de créer un type de bouteille
     * @param designation: désignation (en kg) du type de bouteille
     * @param marque: marque du type de bouteille
     * @param rembo: le type de bouteilles est-il un réservoir rembo ?
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(
            @RequestParam float designation,
            @RequestParam String marque,
            @RequestParam boolean rembo
    ) {

        // Création, Vérification & Sauvegarde
        TypeBouteille typeBouteille = new TypeBouteille(designation, marque, rembo);
        for (TypeBouteille t : typeBouteilleDao.findAll()) {
            if (t.equals(typeBouteille)) return Utils.getJsonMessage(false, "Le type de bouteille existe déjà !", null);
        }
        typeBouteilleDao.save(typeBouteille);
        return Utils.getJsonMessage(true, "Type de bouteille ajouté avec succès", typeBouteille.getId());

    }

    /**
     * Méthode edit, permet de modifier un type de bouteille par rapport à son id
     * @param id: id du type de bouteille à modifier
     * @param marque: marque du type de bouteille
     * @param rembo: le type de bouteilles est-il un réservoir rembo ?
     * @return: Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(
            @PathVariable("id") String id,
            @RequestParam String marque,
            @RequestParam boolean rembo
    ) {

        // Récupération du type de bouteille
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        TypeBouteille typeBouteille = typeBouteilleDao.findById(UUID.fromString(id));
        if (typeBouteille == null) return Utils.getJsonMessage(false, "Le type de bouteille n'existe pas");

        // Modification & Sauvegarde
        typeBouteille.setMarque(marque);
        typeBouteille.setRembo(rembo);
        typeBouteilleDao.save(typeBouteille);
        return Utils.getJsonMessage(true, "Type de bouteille modifié avec succès", typeBouteille.getId());

    }

    /**
     * Méthode delete, permet de supprimer un type de bouteille par rapport à son id
     * @param id: id du type de bouteille à supprimer
     * @return: Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération du type de bouteille
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        TypeBouteille typeBouteille = typeBouteilleDao.findById(UUID.fromString(id));
        if (typeBouteille == null) return Utils.getJsonMessage(false, "Le type de bouteille n'existe pas");

        // Suppression
        if (bouteilleDao.findByTypeBouteille(typeBouteille).size() > 0) {
            return Utils.getJsonMessage(false, "Le type de bouteille est utilisé et ne peut pas être supprimé !", null);
        } else {
            typeBouteilleDao.delete(typeBouteille);
            return Utils.getJsonMessage(true, "Type de bouteille supprimé avec succès", null);
        }

    }


    @Autowired
    TypeBouteilleDao typeBouteilleDao;

    @Autowired
    BouteilleDao bouteilleDao;

}

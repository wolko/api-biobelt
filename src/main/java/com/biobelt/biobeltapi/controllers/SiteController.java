package com.biobelt.biobeltapi.controllers;

import com.biobelt.biobeltapi.Utils;
import com.biobelt.biobeltapi.models.EtatEvenementDao;
import com.biobelt.biobeltapi.models.SmsDao;
import com.biobelt.biobeltapi.models.interventions.InterventionV2Dao;
import com.biobelt.biobeltapi.models.sites.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 16/04/2017
 */

@CrossOrigin()
@Controller
@RequestMapping("/site")
public class SiteController {

    /**
     * Méthode getAll, permet de récupérer tous les sites
     * @return La liste de tous les sites
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Site> getAll() {
        return siteDao.findAll();
    }

    /**
     * Méthode get, permet de récupérer un site par rapport à son id
     * @param id: id du site à récupérer
     * @return Le site ou null s'il n'existe pas
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") String id) {

        // Récupération du site
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(id));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");
        return site;

    }

    /**
     * Méthode create, permet de créer un site
     * @param nom: nom du site
     * @param numClient: numéro de client du site
     * @param nomResponsable: nom du responsable du site
     * @param telResponsable: téléphone du reponsable ud site
     * @param emailResponsable: email du responsable du site
     * @param infos: infos du site
     * @param lat: latitude du site
     * @param lng: longitude du site
     * @param adresse: adresse du site
     * @return Le résultat de la commande
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(
            @RequestParam String nom,
            @RequestParam String numClient,
            @RequestParam String nomResponsable,
            @RequestParam String telResponsable,
            @RequestParam String emailResponsable,
            @RequestParam String infos,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam String adresse) {

        // Création & Sauvegarde
        Site site = new Site(nom, numClient, nomResponsable, telResponsable, emailResponsable, infos, lat, lng, adresse);
        siteDao.save(site);
        return Utils.getJsonMessage(true, "Site ajouté avec succès", site.getId());

    }

    /**
     * Méthode edit, permet de modifier un site par rapport à son id
     * @param id: id du site à modifier / supprimer
     * @param nom: nom du site
     * @param numClient: numéro de client du site
     * @param nomResponsable: nom du responsable du site
     * @param telResponsable: téléphone du reponsable ud site
     * @param emailResponsable: email du responsable du site
     * @param infos: infos du site
     * @param lat: latitude du site
     * @param lng: longitude du site
     * @param adresse: adresse du site
     * @return: Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(
            @PathVariable("id") String id,
            @RequestParam String nom,
            @RequestParam String numClient,
            @RequestParam String nomResponsable,
            @RequestParam String telResponsable,
            @RequestParam String emailResponsable,
            @RequestParam String infos,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam String adresse
    ) {

        // Récupération du site
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(id));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");


        // Modification & Sauvegarde
        site.setNom(nom);
        site.setNumClient(numClient);
        site.setNomResponsable(nomResponsable);
        site.setTelResponsable(telResponsable);
        site.setEmailResponsable(emailResponsable);
        site.setInfos(infos);
        site.setLatitude(lat);
        site.setLongitude(lng);
        site.setAdresse(adresse);
        siteDao.save(site);
        return Utils.getJsonMessage(true, "Site modifié avec succès", site.getId());

    }

    /**
     * Méthode delete, permet de supprimer un site par rapport à son id
     * @param id: id du site à modifier / supprimer
     * @return: Le résultat de la commande
     */
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable("id") String id) {

        // Récupération du site
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(id));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");


        // Suppression du site
        for (Ceinture c : ceintureDao.findBySite(site)) {
            c.setSite(null);
            ceintureDao.save(c);
        }
        siteDao.delete(site);
        return Utils.getJsonMessage(true, "Site supprimé avec succès", null);

    }


    /**
     * Méthode getCeinturesV2, permet de récupérer toutes les ceintures V2 d'un site par rapport à son id
     * @param id: id du site
     * @return Les ceintures V2 du site
     */
    @RequestMapping(value = "/{id}/ceinturesV2", method = RequestMethod.GET)
    @ResponseBody
    public Object getCeinturesV2(@PathVariable("id") String id) {

        // Récupération du site
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(id));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");


        // Récupération des ceintures V2
        List<CeintureV2> ceinturesV2 = new ArrayList<>();
        for (Ceinture c : ceintureDao.findBySite(site)) {
            if (c instanceof CeintureV2) {
                ceinturesV2.add((CeintureV2) c);
            }
        }
        return ceinturesV2;

    }

    /**
     * Méthode getCeinturesV3, permet de récupérer toutes les ceintures V3 d'un site par rapport à son id
     * @param id: id du site
     * @return Les ceintures V3 du site
     */
    @RequestMapping(value = "/{id}/ceinturesV3", method = RequestMethod.GET)
    @ResponseBody
    public Object getCeinturesV3(@PathVariable("id") String id) {

        // Récupération du site
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(id));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");


        // Récupération des ceintures V3
        List<CeintureV3> ceinturesV3 = new ArrayList<>();
        for (Ceinture c : ceintureDao.findBySite(site)) {
            if (c instanceof CeintureV3) {
                ceinturesV3.add((CeintureV3) c);
            }
        }
        return ceinturesV3;

    }


    /**
     * Méthode getCeinturesV2LastInfos, permet de récupérer toutes les ceintures V2 avec les dernières informations d'un site par rapport à son id
     * @param id: id du site
     * @return Les ceintures V2 du site avec les informations.
     * Attention, si une ceinture est archive, elle ne sera pas présente dans le résultat
     */
    @RequestMapping(value = "{id}/getCeinturesV2LastInfos", method = RequestMethod.GET)
    @ResponseBody
    public Object getCeinturesV2LastInfos(@PathVariable("id") String id) {

        // Récupération du site
        if (!Utils.isValidUUID(id)) return Utils.getJsonMessage(false, "Identifiant invalide");
        Site site = siteDao.findById(UUID.fromString(id));
        if (site == null) return Utils.getJsonMessage(false, "Le site n'existe pas");


        // Récupération des informations
        List<HashMap<String, Object>> infos = new ArrayList<>();
        for (Ceinture c : ceintureDao.findBySite(site)) {
            if (c instanceof CeintureV2 && !c.isArchive()) {
                HashMap<String, Object> ceinture = new HashMap<>();
                ceinture.put("ceinture", c);                                                    // Ajout de la ceinture
                ceinture.put("intervention", interventionV2Dao.findLastOfCeinture(c.getId()));    // Dernière intervention
                ceinture.put("etat", etatEvenementDao.findLastOfCeinture(c.getId()));           // Dernier etat
                ceinture.put("sms", smsDao.findLastOfCeinture(c.getId()));                      // Dernier sms
                infos.add(ceinture);                                                            // Ajout dans la liste
            }
        }
        return infos;

    }


    @Autowired
    SiteDao siteDao;

    @Autowired
    CeintureDao ceintureDao;

    @Autowired
    EtatEvenementDao etatEvenementDao;

    @Autowired
    SmsDao smsDao;

    @Autowired
    InterventionV2Dao interventionV2Dao;

}

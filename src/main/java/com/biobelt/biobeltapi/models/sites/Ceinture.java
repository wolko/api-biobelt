package com.biobelt.biobeltapi.models.sites;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 16/04/2017
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // Une table par classes filles
public abstract class Ceinture{

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="Nom")
    private String nom;

    @Column(name="UPC")
    private String upc;

    @Column(name = "NombrePieges")
    private int nombrePieges;

    @Column(name = "Commentaire")
    private String commentaire;

    @Column(name = "Hivernage")
    private boolean hivernage;

    @Column(name = "Archive")
    private boolean archive;

    @ManyToOne
    private Site site;


    /*
     * Constructeurs
     */
    public Ceinture(){}

    public Ceinture(String nom, String upc, int nombrePieges, String commentaire, boolean hivernage, Site site) {
        this.nom = nom;
        this.upc = upc;
        this.nombrePieges = nombrePieges;
        this.commentaire = commentaire;
        this.hivernage = hivernage;
        this.archive = false;
        this.site = site;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getUpc() {
        return upc;
    }

    public int getNombrePieges() {
        return nombrePieges;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public boolean isHivernage() {
        return hivernage;
    }

    public boolean isArchive() {
        return archive;
    }

    public Site getSite() {
        return site;
    }


    /*
     * Setters
     */
    public void setNom(String nom) {
        this.nom = nom;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setUpc(String upc) {
        this.upc = upc;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setNombrePieges(int nombrePieges) {
        this.nombrePieges = nombrePieges;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setHivernage(boolean hivernage) {
        this.hivernage = hivernage;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setSite(Site site) {
        this.site = site;
        if (getSite() != null) getSite().randomIdVersion();
    }

}

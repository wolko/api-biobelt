package com.biobelt.biobeltapi.models.sites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @since 18/06/2017
 * @version 1.0.1
 */

@Entity
@Table(name = "site")
public class Site {

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="Nom")
    private String nom;

    @Column(name="NumClient")
    private String numClient;

    @Column(name="NomRepsonsable")
    private String nomResponsable;

    @Column(name="TelResponsable")
    private String telResponsable;

    @Column(name="EmailResponsable")
    private String emailResponsable;

    @Column(name="Infos")
    private String infos;

    @Column(name="Latitude")
    private double latitude;

    @Column(name="Longitude")
    private double longitude;

    @Column(name="Adresse")
    private String adresse;

    @Column(name="IdVersion")
    private UUID idVersion;


    /*
     * Constructeurs
     */
    public Site(){}

    public Site(String nom) {
        this.nom = nom;
    }

    public Site(
            String nom,
            String numClient,
            String nomResponsable,
            String telResponsable,
            String emailResponsable,
            String infos,
            double latitude,
            double longitude,
            String adresse) {
        this.nom = nom;
        this.numClient = numClient;
        this.nomResponsable = nomResponsable;
        this.telResponsable = telResponsable;
        this.emailResponsable = emailResponsable;
        this.infos = infos;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.idVersion = UUID.randomUUID(); // Génération d'un id de version de base
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

    public String getNumClient() {
        return numClient;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public String getTelResponsable() {
        return telResponsable;
    }

    public String getEmailResponsable() {
        return emailResponsable;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public UUID getIdVersion() {
        return idVersion;
    }

    public String getInfos() {
        return infos;
    }


    /*
     * Setters
     */

    public void setNom(String nom) {
        this.nom = nom;
        this.randomIdVersion();
    }

    public void setNumClient(String numClient) {
        this.numClient = numClient;
        this.randomIdVersion();
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
        this.randomIdVersion();
    }

    public void setTelResponsable(String telResponsable) {
        this.telResponsable = telResponsable;
        this.randomIdVersion();
    }

    public void setEmailResponsable(String emailResponsable) {
        this.emailResponsable = emailResponsable;
        this.randomIdVersion();
    }

    public void setInfos(String infos) {
        this.infos = infos;
        this.randomIdVersion();
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.randomIdVersion();
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.randomIdVersion();
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
        this.randomIdVersion();
    }

    public void randomIdVersion() {
        this.idVersion = UUID.randomUUID();
    }

}

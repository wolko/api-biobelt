package com.biobelt.biobeltapi.models;

import com.biobelt.biobeltapi.models.sites.Ceinture;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author damy
 * @since 16/04/2017
 * @version 1.0.1
 */

@Entity
@Table(name = "Sms")
public class Sms extends Evenement{

    /*
     * Attributs
     */
    @Column(name = "Contenu")
    @NotNull
    private String contenu;

    //Sauveegarde du numéro en moment de l'envoi du SMS
    //Important si la sites n'est pas connue au moment de la réception
    @NotNull
    @Column(name = "NumeroTelephoneCeinture")
    private String numeroTelephoneCeinture;

    @Column(name = "DateReception")
    private LocalDateTime dateReception;

    @Column(name = "Supprime")
    private Boolean supprime;

    @Column(name = "Edite")
    private Boolean edite;


    /*
     * Constructeurs
     */
    public Sms() { super();}

    public Sms(
            Ceinture ceinture,
            String contenu,
            String numeroTelephoneCeinture,
            LocalDateTime date,
            LocalDateTime dateReception,
            boolean edite,
            boolean supprime
    ) {
        this.ceinture = ceinture;
        this.contenu = contenu;
        this.numeroTelephoneCeinture = numeroTelephoneCeinture;
        this.date = date;
        this.dateReception = dateReception;
        this.edite = edite;
        this.supprime = supprime;
    }


    /*
     * Getters
     */
    public String getContenu() {
        return contenu;
    }

    public String getNumeroTelephoneCeinture() {
        return numeroTelephoneCeinture;
    }

    public LocalDateTime getDateReception() {
        return dateReception;
    }

    public Boolean getSupprime() {
        return supprime;
    }

    public Boolean getEdite() {
        return edite;
    }

}


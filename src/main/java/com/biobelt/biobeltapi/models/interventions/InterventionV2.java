package com.biobelt.biobeltapi.models.interventions;

import com.biobelt.biobeltapi.models.Evenement;
import com.biobelt.biobeltapi.models.sites.Ceinture;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 16/04/2017
 */

@Entity
@Table(name = "InterventionV2")
public class InterventionV2 extends Evenement {

    /*
     * Attributs
     */
    @Column(name = "Objet")
    private String objet;

    @Column(name = "Operateur")
    private String operateur;

    @Column(name = "FicheUrl")
    private String ficheUrl;

    @Column(name = "NouveauDebit")
    private float nouveauDebit;

    @Column(name = "DebitAvant")
    private float debitAvant;
    @Column(name = "DebitApres")
    private float debitApres;

    @Column(name = "PressionB1Avant")
    private float pressionB1Avant;
    @Column(name = "PressionB1Apres")
    private float pressionB1Apres;

    @Column(name = "PressionB2Avant")
    private float pressionB2Avant;
    @Column(name = "PressionB2Apres")
    private float pressionB2Apres;

    @Column(name = "PressionSortieAvant")
    private float pressionSortieAvant;
    @Column(name = "PressionSortieApres")
    private float pressionSortieApres;

    @Column(name = "B1ActiveAvant")
    private boolean b1ActiveAvant;
    @Column(name = "B1ActiveApres")
    private boolean b1ActiveApres;


    /*
     * Constructeurs
     */
    public InterventionV2() {
        super();
    }

    public InterventionV2(String objet, String operateur, String ficheUrl, float nouveauDebit, LocalDateTime date, Ceinture ceinture,
                          float debitAvant, float debitApres, float pressionB1Avant, float pressionB1Apres, float pressionB2Avant, float pressionB2Apres, float pressionSortieAvant, float pressionSortieApres, boolean b1ActiveAvant, boolean b1ActiveApres) {
        super();

        this.objet = objet;
        this.operateur = operateur;
        this.ficheUrl = ficheUrl;
        this.nouveauDebit = nouveauDebit;
        this.date = date;
        this.ceinture = ceinture;

        this.debitAvant = debitAvant;
        this.debitApres = debitApres;
        this.pressionB1Avant = pressionB1Avant;
        this.pressionB1Apres = pressionB1Apres;
        this.pressionB2Avant = pressionB2Avant;
        this.pressionB2Apres = pressionB2Apres;
        this.pressionSortieAvant = pressionSortieAvant;
        this.pressionSortieApres = pressionSortieApres;
        this.b1ActiveAvant = b1ActiveAvant;
        this.b1ActiveApres = b1ActiveApres;
    }


    /*
     * Getters
     */
    public String getOperateur() {
        return operateur;
    }

    public float getNouveauDebit() {
        return nouveauDebit;
    }

    public String getObjet() {
        return objet;
    }

    public String getFicheUrl() {
        return ficheUrl;
    }

    public float getDebitAvant() {
        return debitAvant;
    }

    public float getDebitApres() {
        return debitApres;
    }

    public float getPressionB1Avant() {
        return pressionB1Avant;
    }

    public float getPressionB1Apres() {
        return pressionB1Apres;
    }

    public float getPressionB2Avant() {
        return pressionB2Avant;
    }

    public float getPressionB2Apres() {
        return pressionB2Apres;
    }

    public float getPressionSortieAvant() {
        return pressionSortieAvant;
    }

    public float getPressionSortieApres() {
        return pressionSortieApres;
    }

    public boolean isB1ActiveAvant() {
        return b1ActiveAvant;
    }

    public boolean isB1ActiveApres() {
        return b1ActiveApres;
    }


    /*
     * Setters
     */
    public void setOperateur(String operateur) {
        this.operateur = operateur;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setNouveauDebit(float nouveauDebit) {
        this.nouveauDebit = nouveauDebit;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setObjet(String objet) {
        this.objet = objet;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setFicheUrl(String ficheUrl) {
        this.ficheUrl = ficheUrl;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setDebitAvant(float debitAvant) {
        this.debitAvant = debitAvant;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setDebitApres(float debitApres) {
        this.debitApres = debitApres;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setPressionB1Avant(float pressionB1Avant) {
        this.pressionB1Avant = pressionB1Avant;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setPressionB1Apres(float pressionB1Apres) {
        this.pressionB1Apres = pressionB1Apres;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setPressionB2Avant(float pressionB2Avant) {
        this.pressionB2Avant = pressionB2Avant;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setPressionB2Apres(float pressionB2Apres) {
        this.pressionB2Apres = pressionB2Apres;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setPressionSortieAvant(float pressionSortieAvant) {
        this.pressionSortieAvant = pressionSortieAvant;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setPressionSortieApres(float pressionSortieApres) {
        this.pressionSortieApres = pressionSortieApres;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setB1ActiveAvant(boolean b1ActiveAvant) {
        this.b1ActiveAvant = b1ActiveAvant;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setB1ActiveApres(boolean b1ActiveApres) {
        this.b1ActiveApres = b1ActiveApres;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }


    /*
     * Méthode toString
     */
    public String toString(){
        return "Intervention : [" +
                "Date : " + this.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) +
                "]";
    }

}

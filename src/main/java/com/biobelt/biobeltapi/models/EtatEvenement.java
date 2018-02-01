package com.biobelt.biobeltapi.models;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author damy
 * @since 16/04/2017
 * @version 1.0.1
 */

@Entity
@Table(name = "EtatEvenement")
public class EtatEvenement {

    /*
     * Attributs
     */
    @Id
    @Column(name = "Id", columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @OneToOne
    private Evenement evenement;

    @Column(name = "Statut")
    private int statut;

    @Column(name = "ReserveActive")
    private int reserveActive;

    @Column(name = "IsIntervention")
    private Boolean isIntervention;

    @Column(name = "DebitActuel")
    private float debitActuel; //en kg/h

    @Column(name = "EtatB1")
    private float etatB1;

    @Column(name = "EtatB2")
    private float etatB2;

    @Column(name = "DureeB1")
    private int dureeB1;

    @Column(name = "DureeB2")
    private int dureeB2;

    @Column(name = "DureeDiffusionTotal")
    private float dureeDiffusionTotal;

    @Column(name = "QuantiteDiffuseeTotal")
    private float quantiteDiffuseeTotal;

    @Column(name = "DureeDiffusionIntervention")
    private float dureeDiffusionIntervention;

    @Column(name = "QuantiteDiffuseeIntervention")
    private float quantiteDiffuseeIntervention;

    @Column(name = "DureeDiffusionPa")
    private float dureeDiffusionPa;

    @Column(name = "QuantiteDiffuseePa")
    private float quantiteDiffuseePa;

    @Column(name = "isDiffusionNormale")
    private boolean isDiffusionNormale;

    @Column(name = "isDiffusionBoost")
    private boolean isDiffusionBoost;

    @Column(name = "isEnMarche") // isMacron
    private boolean isEnMarche;


    /*
     * Constructeurs
     */
    public EtatEvenement() {
        this.statut = 0;
        this.reserveActive = 1;
        this.isIntervention = false;
        this.etatB1 = 0;
        this.etatB2 = 0;

        this.dureeB1 = 0;
        this.dureeB2 = 0;

        this.dureeDiffusionTotal = 0f;
        this.dureeDiffusionIntervention = 0f;
        this.dureeDiffusionPa = 0f;

        this.quantiteDiffuseeTotal = 0f;
        this.quantiteDiffuseeIntervention = 0f;
        this.quantiteDiffuseePa = 0f;

        this.isDiffusionNormale = false;
        this.isDiffusionBoost = false;

        this.debitActuel = 0f;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public float getDebitActuel() {
        return debitActuel;
    }

    public int getStatut() {
        return statut;
    }

    public Boolean getIntervention() {
        return isIntervention;
    }

    public int getReserveActive() {
        return reserveActive;
    }

    public float getEtatB1() {
        return etatB1;
    }

    public float getEtatB2() {
        return etatB2;
    }

    public int getDureeB1() {
        return dureeB1;
    }

    public int getDureeB2() {
        return dureeB2;
    }

    public float getDureeDiffusionTotal() {
        return dureeDiffusionTotal;
    }

    public float getQuantiteDiffuseeTotal() {
        return quantiteDiffuseeTotal;
    }

    public float getDureeDiffusionIntervention() {
        return dureeDiffusionIntervention;
    }

    public float getQuantiteDiffuseeIntervention() {
        return quantiteDiffuseeIntervention;
    }

    public float getDureeDiffusionPa() {
        return dureeDiffusionPa;
    }

    public float getQuantiteDiffuseePa() {
        return quantiteDiffuseePa;
    }

    public boolean isDiffusionNormale() {
        return isDiffusionNormale;
    }

    public boolean isDiffusionBoost() {
        return isDiffusionBoost;
    }

    public boolean isEnMarche() {
        return isEnMarche;
    }


    /*
     * Setters
     */
    public void setId(UUID id) {
        this.id = id;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public void setDebitActuel(float debitActuel) {
        this.debitActuel = debitActuel;
    }

    public void setIntervention(Boolean intervention) {
        isIntervention = intervention;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public void setReserveActive(int reserveActive) {
        this.reserveActive = reserveActive;
    }

    public void setEtatB1(float etatB1) {
        this.etatB1 = etatB1;
    }

    public void setEtatB2(float etatB2) {
        this.etatB2 = etatB2;
    }

    public void setDureeB1(int dureeB1) {
        this.dureeB1 = dureeB1;
    }

    public void setDureeB2(int dureeB2) {
        this.dureeB2 = dureeB2;
    }

    public void setDureeDiffusionTotal(float dureeDiffusionTotal) {
        this.dureeDiffusionTotal = dureeDiffusionTotal;
    }

    public void setQuantiteDiffuseeTotal(float quantiteDiffuseeTotal) {
        this.quantiteDiffuseeTotal = quantiteDiffuseeTotal;
    }

    public void setDureeDiffusionIntervention(float dureeDiffusionIntervention) {
        this.dureeDiffusionIntervention = dureeDiffusionIntervention;
    }

    public void setQuantiteDiffuseeIntervention(float quantiteDiffuseeIntervention) {
        this.quantiteDiffuseeIntervention = quantiteDiffuseeIntervention;
    }

    public void setDureeDiffusionPa(float dureeDiffusionPa) {
        this.dureeDiffusionPa = dureeDiffusionPa;
    }

    public void setQuantiteDiffuseePa(float quantiteDiffuseePa) {
        this.quantiteDiffuseePa = quantiteDiffuseePa;
    }

    public void setDiffusionNormale(boolean diffusionNormale) {
        isDiffusionNormale = diffusionNormale;
    }

    public void setDiffusionBoost(boolean diffusionBoost) {
        isDiffusionBoost = diffusionBoost;
    }

    public void setEnMarche(boolean enMarche) {
        isEnMarche = enMarche;
    }


    /*
     * Méthode copy
     */
    public void copy(EtatEvenement someOtherEtatEvenement){

        this.setReserveActive(someOtherEtatEvenement.getReserveActive());
        this.setEtatB1(someOtherEtatEvenement.getEtatB1());
        this.setEtatB2(someOtherEtatEvenement.getEtatB2());

        this.setDureeDiffusionTotal(someOtherEtatEvenement.getDureeDiffusionTotal());
        this.setQuantiteDiffuseeTotal(someOtherEtatEvenement.getQuantiteDiffuseeTotal());
        this.setDureeDiffusionIntervention(someOtherEtatEvenement.getDureeDiffusionIntervention());
        this.setQuantiteDiffuseeIntervention(someOtherEtatEvenement.getDureeDiffusionTotal());

        this.setDiffusionNormale(someOtherEtatEvenement.isDiffusionNormale());
        this.setDiffusionBoost(someOtherEtatEvenement.isDiffusionBoost());
        this.setEnMarche(someOtherEtatEvenement.isEnMarche());

        this.setDebitActuel(someOtherEtatEvenement.getDebitActuel());
    }


    /*
     * Méthode copyWithoutEtats
     */
    public void copyWithoutEtats(EtatEvenement someOtherEtatEvenement){

        this.setReserveActive(someOtherEtatEvenement.getReserveActive());

        this.setDureeDiffusionTotal(someOtherEtatEvenement.getDureeDiffusionTotal());
        this.setQuantiteDiffuseeTotal(someOtherEtatEvenement.getQuantiteDiffuseeTotal());
        this.setDureeDiffusionIntervention(someOtherEtatEvenement.getDureeDiffusionIntervention());
        this.setQuantiteDiffuseeIntervention(someOtherEtatEvenement.getDureeDiffusionTotal());

        this.setDiffusionNormale(someOtherEtatEvenement.isDiffusionNormale());
        this.setDiffusionBoost(someOtherEtatEvenement.isDiffusionBoost());
        this.setEnMarche(someOtherEtatEvenement.isEnMarche());

        this.setDebitActuel(someOtherEtatEvenement.getDebitActuel());
    }


    /*
     * Méthode toString
     */
    @Override
    public String toString(){

        String type = "Sms";
        if(this.isIntervention)
            type = "Intervention";

        return "EtatEvenement : [ Type : " + type + ", "+
                "date : " + this.evenement.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + ", "+
                "B1 : " + this.getEtatB1() + ", "+
                "B2 : " + this.getEtatB2() + ", "+
                "Débit : " + this.getDebitActuel() + ", "+
                "Quantite diff tot. : " + this.getQuantiteDiffuseeTotal() + ", "+
                "Quantite diff int. : " + this.getQuantiteDiffuseeIntervention() + ", "+
                "Quantite diff pa : " + this.getQuantiteDiffuseePa() +
                "]";
    }

}
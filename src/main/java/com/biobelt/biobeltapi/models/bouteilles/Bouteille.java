package com.biobelt.biobeltapi.models.bouteilles;

import com.biobelt.biobeltapi.models.interventions.InterventionV2;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 29/06/2017
 */

@Entity
@Table(name = "Bouteille")
public class Bouteille {

    public enum Etat {
        NEUVE,
        UTILISE
    }

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @Column(name="Remplissage")
    private float remplissage;

    @Column(name="B1")
    private boolean b1;

    @Enumerated(EnumType.STRING)
    private Etat etat;

    @ManyToOne
    private TypeBouteille typeBouteille;

    @ManyToOne
    private InterventionV2 interventionV2;

    /*
     * Constructeurs
     */
    public Bouteille() {}

    public Bouteille(TypeBouteille typeBouteille, float remplissage, boolean b1, Etat etat, InterventionV2 interventionV2) {
        this.typeBouteille = typeBouteille;
        this.remplissage = remplissage;
        this.b1 = b1;
        this.etat = etat;
        this.interventionV2 = interventionV2;
    }

    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public float getRemplissage() {
        return remplissage;
    }

    public boolean isB1() {
        return b1;
    }

    public Etat getEtat() {
        return etat;
    }

    public TypeBouteille getTypeBouteille() {
        return typeBouteille;
    }

    public InterventionV2 getIntervention() {
        return interventionV2;
    }

    /*
     * Setters
     */
    public void setRemplissage(float remplissage) {
        this.remplissage = remplissage;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

    public void setTypeBouteille(TypeBouteille typeBouteille) {
        this.typeBouteille = typeBouteille;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

    public void setInterventionV2(InterventionV2 interventionV2) {
        this.interventionV2 = interventionV2;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }


    /*
     * Méthode toString
     */
    @Override
    public String toString(){
        return "Bouteille : [" +
                "Etat : " + this.getEtat() + ", "+
                "Designation : " + this.getTypeBouteille().getDesignation() + ", "+
                "Date : " + this.getIntervention().getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) +
                "]";
    }
}

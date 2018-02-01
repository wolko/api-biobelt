package com.biobelt.biobeltapi.models.bouteilles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @since 20/06/2017
 * @version 1.0.1
 */

@Entity
@Table(name = "typeBouteille")
public class TypeBouteille {

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="Designation")
    private float designation;

    @Column(name="Marque")
    private String marque;

    @NotNull
    @Column(name="Rembo")
    private boolean rembo;


    /*
     * Constructeurs
     */
    public TypeBouteille() {}

    public TypeBouteille(float designation, String marque, boolean rembo) {
        this.designation = designation;
        this.marque = marque;
        this.rembo = rembo;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public float getDesignation() {
        return designation;
    }

    public String getMarque() {
        return marque;
    }

    public boolean isRembo() {
        return rembo;
    }


    /*
     * Setters
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setRembo(boolean rembo) {
        this.rembo = rembo;
    }


    /*
     * Méthode equals
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TypeBouteille)) return false;

        TypeBouteille typeBouteille = (TypeBouteille) obj;

        return !(this.designation != typeBouteille.designation) && this.marque.equals(typeBouteille.marque) && this.rembo == typeBouteille.rembo;
    }
}

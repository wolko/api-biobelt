package com.biobelt.biobeltapi.models.operateurs;

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
@Table(name = "operateurs")
public class Operateur {

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="Nom")
    private String nom;

    @NotNull
    @Column(name="Prenom")
    private String prenom;


    /*
     * Constructeurs
     */
    public Operateur() {}

    public Operateur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
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

    public String getPrenom() {
        return prenom;
    }


    /*
     * Setters
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}

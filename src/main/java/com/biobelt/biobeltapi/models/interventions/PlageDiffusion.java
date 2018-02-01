package com.biobelt.biobeltapi.models.interventions;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 22/06/2017
 */

@Entity
@Table(name = "plagediffusion")
public class PlageDiffusion {

    /*
     * Attributes
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "Debut")
    private Time debut;

    @NotNull
    @Column(name = "Fin")
    private Time fin;

    @NotNull
    @Column(name = "Combinaison")
    private int combinaison;

    @Column(name = "Avant")
    private boolean avant;

    @ManyToOne
    private InterventionV2 interventionV2;


    /*
     * Constructeurs
     */
    public PlageDiffusion() {}

    public PlageDiffusion(Time debut, Time fin, int combinaison, boolean avant, InterventionV2 interventionV2) {
        this.debut = debut;
        this.fin = fin;
        this.combinaison = combinaison;
        this.avant = avant;
        this.interventionV2 = interventionV2;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public Time getDebut() {
        return debut;
    }

    public Time getFin() {
        return fin;
    }

    public int getCombinaison() {
        return combinaison;
    }

    public boolean isAvant() {
        return avant;
    }

    public InterventionV2 getInterventionV2() {
        return interventionV2;
    }
}

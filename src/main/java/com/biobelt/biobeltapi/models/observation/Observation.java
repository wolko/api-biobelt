package com.biobelt.biobeltapi.models.observation;

import com.biobelt.biobeltapi.models.interventions.InterventionV2;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 21/06/2017
 */

@Entity
@Table(name = "Observation")
public class Observation {

    /*
     * Attributes
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @Column(name = "Objet")
    private String objet;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    private InterventionV2 interventionV2;


    /*
     * Constructeurs
     */
    public Observation() {}

    public Observation(String objet, String description, InterventionV2 interventionV2) {
        this.objet = objet;
        this.description = description;
        this.interventionV2 = interventionV2;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public String getObjet() {
        return objet;
    }

    public String getDescription() {
        return description;
    }

    public InterventionV2 getInterventionV2() {
        return interventionV2;
    }


    /*
     * Setters
     */
    public void setObjet(String objet) {
        this.objet = objet;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

    public void setDescription(String description) {
        this.description = description;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

}

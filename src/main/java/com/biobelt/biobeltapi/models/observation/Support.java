package com.biobelt.biobeltapi.models.observation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 29/06/2017
 */

@Entity
@Table(name = "support")
public class Support {

    public enum Type {
        IMAGE,
        SON,
        VIDEO
    }

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="Url")
    private String url;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Observation observation;

    /*
     * Constructeurs
     */
    public Support() {}

    public Support(String url, Type type, Observation observation) {
        this.url = url;
        this.type = type;
        this.observation = observation;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Type getType() {
        return type;
    }

    public Observation getObservation() {
        return observation;
    }

}

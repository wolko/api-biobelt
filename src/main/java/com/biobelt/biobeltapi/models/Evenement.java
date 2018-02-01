package com.biobelt.biobeltapi.models;

import com.biobelt.biobeltapi.models.sites.Ceinture;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author damy
 * @since 18/07/2017
 * @version 1.0.1
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // Une table par classes filles
public abstract class Evenement {

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    protected UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "Date")
    protected LocalDateTime date;

    @ManyToOne
    protected Ceinture ceinture;


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Ceinture getCeinture() {
        return ceinture;
    }


    /*
     * Setters
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }

    public void setCeinture(Ceinture ceinture) {
        this.ceinture = ceinture;
        if (ceinture != null && ceinture.getSite() != null) ceinture.getSite().randomIdVersion();
    }
}

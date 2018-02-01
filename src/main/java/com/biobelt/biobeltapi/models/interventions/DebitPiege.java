package com.biobelt.biobeltapi.models.interventions;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 23/06/2017
 */

@Entity
@Table(name = "DebitPiege")
public class DebitPiege {

    /*
     * Attributs
     */
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="Debit")
    private float debit;

    @Column(name="NumeroPiege")
    private int numeroPiege;

    @ManyToOne
    private InterventionV2 interventionV2;


    /*
     * Constructeurs
     */
    public DebitPiege() {}

    public DebitPiege(float debit, int numeroPiege, InterventionV2 interventionV2) {
        this.debit = debit;
        this.numeroPiege = numeroPiege;
        this.interventionV2 = interventionV2;
    }


    /*
     * Getters
     */
    public UUID getId() {
        return id;
    }

    public float getDebit() {
        return debit;
    }

    public int getNumeroPiege() {
        return numeroPiege;
    }

    public InterventionV2 getIntervention() {
        return interventionV2;
    }


    /*
     * Setters
     */
    public void setDebit(float debit) {
        this.debit = debit;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

    public void setNumeroPiege(int numeroPiege) {
        this.numeroPiege = numeroPiege;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

    public void setIntervention(InterventionV2 interventionV2) {
        this.interventionV2 = interventionV2;
        if (interventionV2 != null && interventionV2.getCeinture() != null && interventionV2.getCeinture().getSite() != null) interventionV2.getCeinture().getSite().randomIdVersion();
    }

}

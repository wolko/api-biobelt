package com.biobelt.biobeltapi.models.sites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 28/06/2017
 */

@Entity
@Table(name="CeintureV2", uniqueConstraints={@UniqueConstraint(columnNames={"Nom"})})
public class CeintureV2 extends Ceinture {

    /*
     * Attributs
     */
    @NotNull
    @Column(name="NumeroTelephone")
    private String numeroTelephone;

    @Column(name = "InversionEmpty")
    private boolean inversionEmpty;


    /*
     * Constructeurs
     */
    public CeintureV2() {
        super();
    }

    public CeintureV2(String nom, String upc, int nombrePieges, String commentaire, boolean hivernage, Site site, String numeroTelephone, boolean inversionEmpty) {
        super(nom, upc, nombrePieges, commentaire, hivernage, site);
        this.numeroTelephone = numeroTelephone;
        this.inversionEmpty = inversionEmpty;
    }


    /*
     * Getters
     */
    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public boolean isInversionEmpty() {
        return inversionEmpty;
    }


    /*
     * Setters
     */
    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
        if (getSite() != null) getSite().randomIdVersion();
    }

    public void setInversionEmpty(boolean inversionEmpty) {
        this.inversionEmpty = inversionEmpty;
        if (getSite() != null) getSite().randomIdVersion();
    }
}

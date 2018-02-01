package com.biobelt.biobeltapi.models.sites;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Simon WOLKIEWIEZ
 * @version 1.0.1
 * @since 28/06/2017
 */

@Entity
@Table(name="CeintureV3", uniqueConstraints={@UniqueConstraint(columnNames={"Nom"})})
public class CeintureV3 extends Ceinture {

    /*
     * Constructeur
     */
    public CeintureV3(String nom, String upc, int nombrePieges, String commentaire, boolean hivernage, Site site) {
        super(nom, upc, nombrePieges, commentaire, hivernage, site);
    }

}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.authority.lattes.model;

import java.util.List;

public class Person {
    protected String idCNPq;
    protected String name;
    protected List<String> nameVariants;
    protected String firstName;
    protected String lastName;
    protected String institution;

    public String getIdCNPq() {
        return idCNPq;
    }

    public void setIdCNPq(String idCNPq) {
        this.idCNPq = idCNPq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNameVariants() {
        return nameVariants;
    }

    public void setNameVariants(List<String> nameVariants) {
        this.nameVariants = nameVariants;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}

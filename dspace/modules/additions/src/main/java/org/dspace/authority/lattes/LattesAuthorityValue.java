/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.authority.lattes;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.AuthorityValueGenerator;
import org.dspace.authority.PersonAuthorityValue;
import org.dspace.authority.lattes.model.Person;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class LattesAuthorityValue extends PersonAuthorityValue {

    private String cnpq_id;
    private boolean update;

    /**
     * Creates an instance of LattesAuthorityValue with only uninitialized fields.
     * This is meant to be filled in with values from an existing record.
     * To create a brand new LattesAuthorityValue, use create()
     */
    public LattesAuthorityValue() {
    }

    public LattesAuthorityValue(SolrDocument document) {
        super(document);
    }

    public String getCnpq_id() {
        return cnpq_id;
    }

    public void setCnpq_id(String cnpq_id) {
        this.cnpq_id = cnpq_id;
    }

    @Override
    public SolrInputDocument getSolrInputDocument() {
        SolrInputDocument doc = super.getSolrInputDocument();
        if (StringUtils.isNotBlank(getCnpq_id())) {
            doc.addField("cnpq_id", getCnpq_id());
        }
        return doc;
    }

    @Override
    public void setValues(SolrDocument document) {
        super.setValues(document);
        this.cnpq_id = String.valueOf(document.getFieldValue("cnpq_id"));
    }

    public static LattesAuthorityValue create() {
        LattesAuthorityValue lattesAuthorityValue = new LattesAuthorityValue();
        lattesAuthorityValue.setId(UUID.randomUUID().toString());
        lattesAuthorityValue.updateLastModifiedDate();
        lattesAuthorityValue.setCreationDate(new Date());
        return lattesAuthorityValue;
    }

    /**
     * Create an authority based on a given person
     */
    public static LattesAuthorityValue create(Person person) {
        LattesAuthorityValue authority = LattesAuthorityValue.create();
        authority.setValues(person);
        authority.setId("lattes::"+person.getIdCNPq());
        return authority;
    }

    public boolean setValues(Person person) {
        if (updateValue(person.getIdCNPq(), getCnpq_id())) {
            setCnpq_id(person.getIdCNPq());
        }

        if (updateValue(person.getLastName(), getLastName())) {
            setLastName(person.getLastName());
        }

        if (updateValue(person.getFirstName(), getFirstName())) {
            setFirstName(person.getFirstName());
        }

        for (String otherName : person.getNameVariants()) {
            if (!getNameVariants().contains(otherName)) {
                addNameVariant(otherName);
                update = true;
            }
        }

        if (updateValue(person.getInstitution(), getInstitution())) {
            setInstitution(person.getInstitution());
        }

        setValue(getName());

        if (update) {
            update();
        }
        boolean result = update;
        update = false;
        return result;
    }

    private boolean updateValue(String incoming, String resident) {
        boolean update = StringUtils.isNotBlank(incoming) && !incoming.equals(resident);
        if (update) {
            this.update = true;
        }
        return update;
    }

    @Override
    public Map<String, String> choiceSelectMap() {

        Map<String, String> map = super.choiceSelectMap();

        map.put("lattes", getCnpq_id());

        return map;
    }

    public String getAuthorityType() {
        return "lattes";
    }

    @Override
    public String generateString() {
        String generateString = AuthorityValueGenerator.GENERATE + getAuthorityType() + AuthorityValueGenerator.SPLIT;
        if (StringUtils.isNotBlank(getCnpq_id())) {
            generateString += getCnpq_id();
        }
        return generateString;
    }


    @Override
    public AuthorityValue newInstance(String info) {
        AuthorityValue authorityValue;
        if (StringUtils.isNotBlank(info)) {
            Lattes lattes = Lattes.getLattes();
            authorityValue = lattes.queryAuthorityID(info);
        } else {
            authorityValue = LattesAuthorityValue.create();
        }
        return authorityValue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LattesAuthorityValue that = (LattesAuthorityValue) o;

        return !(cnpq_id != null ? !cnpq_id.equals(that.cnpq_id) : that.cnpq_id != null);

    }

    @Override
    public int hashCode() {
        return cnpq_id != null ? cnpq_id.hashCode() : 0;
    }

    public boolean hasTheSameInformationAs(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.hasTheSameInformationAs(o)) {
            return false;
        }

        LattesAuthorityValue that = (LattesAuthorityValue) o;

        return !(cnpq_id != null ? !cnpq_id.equals(that.cnpq_id) : that.cnpq_id != null);
    }
}

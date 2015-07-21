/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.authority.lattes;

import org.apache.log4j.Logger;
import org.dspace.authority.lattes.model.Person;
import org.dspace.authority.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMLtoPerson {
    private static Logger log = Logger.getLogger(XMLtoPerson.class);

    public static List<Person> convert(Document xml) {
        List<Person> result = new ArrayList<>();
        try {
            Iterator<Node> iterator = XMLUtils.getNodeListIterator(xml, "//person");
            while (iterator.hasNext()) {
                Person person = convertPerson(iterator.next());
                result.add(person);
            }
        } catch (XPathExpressionException e) {
            log.error("Error in xpath syntax", e);
        }
        return result;
    }

    private static Person convertPerson(Node node) throws XPathExpressionException {
        Person person = new Person();

        person.setIdCNPq(XMLUtils.getTextContent(node, "idCNPq"));
        person.setName(XMLUtils.getTextContent(node, "name"));

        person.setNameVariants(new ArrayList<String>());
        Iterator<Node> iterator = XMLUtils.getNodeListIterator(node, "nameVariant");
        while (iterator.hasNext()) {
            Node nameVariant = iterator.next();
            person.getNameVariants().add(nameVariant.getTextContent());
        }

        person.setFirstName(XMLUtils.getTextContent(node, "firstName"));
        person.setLastName(XMLUtils.getTextContent(node, "lastName"));
        person.setInstitution(XMLUtils.getTextContent(node, "institution"));

        return person;
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.authority.lattes;

import org.apache.log4j.Logger;
import org.dspace.authority.AuthorityValue;
import org.dspace.authority.lattes.model.Person;
import org.dspace.authority.rest.RestSource;
import org.dspace.utils.DSpace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Lattes extends RestSource {
    private static Logger log = Logger.getLogger(Lattes.class);

    private static Lattes lattes;

    public static Lattes getLattes() {
        if (lattes == null) {
            lattes = new DSpace().getServiceManager().getServiceByName("LattesSource", Lattes.class);
        }
        return lattes;
    }

    private Lattes(String url) {
        super(url);
    }

    public List<AuthorityValue> queryAuthorities(String text, int start, int rows) {
        List<AuthorityValue> authorities = new ArrayList<>();
        List<Person> persons;
        try {
            persons = XMLtoPerson.convert(restConnector.get("search?q="
                    + URLEncoder.encode(text, "UTF-8")
                    + "&start=" + start
                    + "&rows=" + rows));
        } catch (UnsupportedEncodingException e) {
            log.error("UTF-8 encoding unsupported", e);
            return authorities;
        }
        for (Person person : persons) {
            authorities.add(LattesAuthorityValue.create(person));
        }
        return authorities;
    }

    @Override
    public List<AuthorityValue> queryAuthorities(String text, int max) {
        return queryAuthorities(text, 0, max);
    }

    @Override
    public AuthorityValue queryAuthorityID(String id) {
        return LattesAuthorityValue.create(
                XMLtoPerson.convert(
                        restConnector.get("byid/" + id)
                ).get(0));
    }
}

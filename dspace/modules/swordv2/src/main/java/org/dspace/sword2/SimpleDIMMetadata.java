/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.sword2;

import com.google.common.collect.Maps;
import org.dspace.content.Metadatum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleDIMMetadata
{
    private List<Map.Entry<String, String>> atom = new ArrayList<>();
    private Metadatum[] metadata;

    public void addAtom(String element, String value)
    {
        this.atom.add(Maps.immutableEntry(element, value));
    }

    public void setMetadata(Metadatum[] metadata) {
        this.metadata = metadata;
    }

    public List<Map.Entry<String, String>> getAtom()
    {
        return atom;
    }

    public Metadatum[] getMetadata() {
        return metadata;
    }
}

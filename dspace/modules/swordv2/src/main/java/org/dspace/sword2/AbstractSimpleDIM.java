/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.sword2;

import org.dspace.content.Item;
import org.dspace.content.Metadatum;
import org.dspace.core.ConfigurationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AbstractSimpleDIM
{
    protected Map<String, String> dcMap = null;
    protected Map<String, String> atomMap = null;
    protected Map<String, String> atomMapInv = null;

    protected void loadMetadataMaps()
    {
        if (this.dcMap == null)
        {
            // we should load our DC map from configuration
            this.dcMap = new HashMap<>();
            Properties props = ConfigurationManager.getProperties("swordv2-server");
            for (Object key : props.keySet())
            {
                String keyString = (String) key;
                if (keyString.startsWith("simpledc."))
                {
                    String k = keyString.substring("simpledc.".length());
                    String v = (String) props.get(key);
                    this.dcMap.put(k, v);
                }
            }
        }

        if (this.atomMap == null || this.atomMapInv == null)
        {
            this.atomMap = new HashMap<>();
            this.atomMapInv = new HashMap<>();
            Properties props = ConfigurationManager.getProperties("swordv2-server");
            for (Object key : props.keySet())
            {
                String keyString = (String) key;
                if (keyString.startsWith("atom."))
                {
                    String k = keyString.substring("atom.".length());
                    String v = (String) props.get(key);
                    this.atomMap.put(k, v);
                    this.atomMapInv.putIfAbsent(v, k);
                }
            }
        }
    }

    protected SimpleDIMMetadata getMetadata(Item item)
    {
        this.loadMetadataMaps();

        SimpleDIMMetadata md = new SimpleDIMMetadata();
        Metadatum[] all = item.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        md.setMetadata(all);

        for (Metadatum dcv : all)
        {
            String valueMatch = dcv.schema + "." + dcv.element;
            if (dcv.qualifier != null)
            {
                valueMatch += "." + dcv.qualifier;
            }

            // look for the metadata in the atom map
            md.addAtom(this.atomMapInv.get(valueMatch), dcv.value);
        }

        return md;
    }
}

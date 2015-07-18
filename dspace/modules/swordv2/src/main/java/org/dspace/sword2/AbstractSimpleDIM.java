/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.sword2;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.dspace.content.Item;
import org.dspace.content.Metadatum;
import org.dspace.core.ConfigurationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AbstractSimpleDIM
{
    protected Map<String, String> dcMap = null;
    protected BiMap<String, String> atomMap = null;

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

        if (this.atomMap == null)
        {
            Map<String, String> atomMap = new HashMap<>();
            Properties props = ConfigurationManager.getProperties("swordv2-server");
            for (Object key : props.keySet())
            {
                String keyString = (String) key;
                if (keyString.startsWith("atom."))
                {
                    String k = keyString.substring("atom.".length());
                    String v = (String) props.get(key);
                    atomMap.put(k, v);
                }
            }
            this.atomMap = ImmutableBiMap.copyOf(atomMap);
        }
    }

    protected SimpleDIMMetadata getMetadata(Item item)
    {
        this.loadMetadataMaps();

        SimpleDIMMetadata md = new SimpleDIMMetadata();
        Metadatum[] all = item.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        md.setMetadata(all);

        Map<String, String> invAtomMap = this.atomMap.inverse();
        for (Metadatum dcv : all)
        {
            String valueMatch = dcv.schema + "." + dcv.element;
            if (dcv.qualifier != null)
            {
                valueMatch += "." + dcv.qualifier;
            }

            // look for the metadata in the atom map
            md.addAtom(invAtomMap.get(valueMatch), dcv.value);
        }

        return md;
    }
}

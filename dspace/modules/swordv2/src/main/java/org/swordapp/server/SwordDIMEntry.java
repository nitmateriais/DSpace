package org.swordapp.server;


import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.dspace.content.Metadatum;
import org.dspace.content.authority.Choices;

import java.util.ArrayList;
import java.util.List;

public class SwordDIMEntry extends SwordEntry
{
    public SwordDIMEntry(Entry entry) {
        super(entry);
    }

    public SwordDIMEntry(SwordEntry swordEntry) {
        this(swordEntry.entry);
    }

    public List<Metadatum> getDIM()
    {
        List<Metadatum> dim = new ArrayList<>();
        List<Element> extensions = this.entry.getExtensions();
        for (Element element : extensions)
        {
            if (DIMUriRegisty.DIM_FIELD.equals(element.getQName()))
            {
                // we have a dim:field element
                Metadatum dcv = new Metadatum();

                dcv.schema = element.getAttributeValue("mdschema");
                dcv.element = element.getAttributeValue("element");
                dcv.qualifier = element.getAttributeValue("qualifier");

                dcv.language = element.getAttributeValue("lang");
                if (dcv.language == null)
                {
                    dcv.language = element.getAttributeValue("language");
                }

                dcv.authority = element.getAttributeValue("authority");
                try
                {
                    dcv.confidence = Integer.parseInt(element.getAttributeValue("confidence"));
                }
                catch (NumberFormatException | NullPointerException e) {
                    dcv.confidence = Choices.CF_UNSET;
                }

                dcv.value = element.getText();

                dim.add(dcv);
            }
        }
        return dim;
    }
}

/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.sword2;

import org.apache.abdera.model.Element;
import org.apache.commons.lang.StringUtils;
import org.dspace.content.Item;
import org.dspace.content.Metadatum;
import org.dspace.content.authority.Choices;
import org.dspace.core.Context;
import org.swordapp.server.DIMUriRegisty;
import org.swordapp.server.DepositReceipt;
import org.swordapp.server.SwordError;
import org.swordapp.server.SwordServerException;

import java.util.List;
import java.util.Map;

public class SimpleDIMEntryDisseminator extends AbstractSimpleDIM implements SwordEntryDisseminator
{
    public SimpleDIMEntryDisseminator() { }

    public DepositReceipt disseminate(Context context, Item item, DepositReceipt receipt)
            throws DSpaceSwordException, SwordError, SwordServerException
    {
        SimpleDIMMetadata md = this.getMetadata(item);

        for (Metadatum dcv : md.getMetadata())
        {
            ElementAttrWrapper e = new ElementAttrWrapper(
                    receipt.getWrappedEntry().addSimpleExtension(DIMUriRegisty.DIM_FIELD, dcv.value));
            e.attr("mdschema", dcv.schema);
            e.attr("element", dcv.element);
            e.attr("qualifier", dcv.qualifier);
            e.attr("lang", dcv.language);
            e.attr("authority", dcv.authority);
            e.attr("confidence",
                    dcv.confidence != Choices.CF_UNSET
                    ? String.valueOf(dcv.confidence)
                    : null);
        }

        final List<Map.Entry<String, String>> atom = md.getAtom();
        for (Map.Entry<String, String> element : atom)
        {
            final String key = element.getKey();
            final String value = element.getValue();
            if ("author".equals(key))
            {
                receipt.getWrappedEntry().addAuthor(value);
            }
            else if ("published".equals(key))
            {
                receipt.getWrappedEntry().setPublished(value);
            }
            else if ("rights".equals(key))
            {
                receipt.getWrappedEntry().setRights(value);
            }
            else if ("summary".equals(key))
            {
                receipt.getWrappedEntry().setSummary(value);
            }
            else if ("title".equals(key))
            {
                receipt.getWrappedEntry().setTitle(value);
            }
            else if ("updated".equals(key))
            {
                receipt.getWrappedEntry().setUpdated(value);
            }
        }

        return receipt;
    }

    private class ElementAttrWrapper
    {
        private Element e;

        public ElementAttrWrapper(Element e)
        {
            this.e = e;
        }

        public void attr(String key, String val)
        {
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(val))
            {
                this.e.setAttributeValue(key, val);
            }
        }
    }
}

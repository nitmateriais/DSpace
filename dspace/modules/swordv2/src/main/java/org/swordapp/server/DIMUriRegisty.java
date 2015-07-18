package org.swordapp.server;

import javax.xml.namespace.QName;

public class DIMUriRegisty extends UriRegistry {
    // Namespaces
    public static String DIM_NAMESPACE = "http://www.dspace.org/xmlns/dspace/dim";

    // QNames for Extension Elements
    public static QName DIM_FIELD = new QName(DIM_NAMESPACE, "field");
}

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.ws.rs.GET
 *  javax.ws.rs.Path
 *  javax.ws.rs.Produces
 *  org.sonatype.goodies.common.ComponentSupport
 *  org.sonatype.nexus.common.property.SystemPropertiesHelper
 *  org.sonatype.nexus.rest.Resource
 */
package org.sonatype.nexus.coreui.internal.wonderland;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.common.property.SystemPropertiesHelper;
import org.sonatype.nexus.coreui.internal.wonderland.PropertyXO;
import org.sonatype.nexus.rest.Resource;

@Named
@Singleton
@Path(value="/wonderland/settings")
public class SettingsResource
extends ComponentSupport
implements Resource {
    public static final String RESOURCE_URI = "/wonderland/settings";

    @GET
    @Produces(value={"application/json", "application/xml"})
    public List<PropertyXO> get() {
        ArrayList properties = Lists.newArrayList();
        properties.add(new PropertyXO().withKey("keepAlive").withValue(Boolean.toString(SystemPropertiesHelper.getBoolean((String)"nexus.ui.keepAlive", (boolean)true))));
        return properties;
    }
}


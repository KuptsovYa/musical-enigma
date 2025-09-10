/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  io.swagger.annotations.ApiParam
 */
package org.sonatype.nexus.security.realm.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.sonatype.nexus.security.realm.api.RealmApiXO;

@Api(value="Security management: realms")
public interface RealmApiResourceDoc {
    @ApiOperation(value="List the available realms")
    public List<RealmApiXO> getRealms();

    @ApiOperation(value="List the active realm IDs in order")
    public List<String> getActiveRealms();

    @ApiOperation(value="Set the active security realms in the order they should be used")
    public void setActiveRealms(@ApiParam(value="The realm IDs") List<String> var1);
}


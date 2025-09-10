/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  javax.annotation.Nullable
 */
package com.sonatype.nexus.pro.systemchecks.internal.status.rest.datastore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import javax.annotation.Nullable;

@ApiModel
public class SystemCheckResultsApiDTO {
    private final String nodeId;
    private final String hostname;
    private final Map<String, SystemCheckResultDTO> results;

    public SystemCheckResultsApiDTO(String nodeId, String hostname, Map<String, SystemCheckResultDTO> results) {
        this.nodeId = nodeId;
        this.hostname = hostname;
        this.results = results;
    }

    @ApiModelProperty(value="A unique identifier for the node, should not be considered stable")
    public String getNodeId() {
        return this.nodeId;
    }

    @ApiModelProperty(value="The hostname of the originating node", example="nexus01")
    public String getHostname() {
        return this.hostname;
    }

    @ApiModelProperty(value="The system status check results", example="{\"Check\": {\"healthy\": false, \"message\": \"An explanation of the check\"}}")
    public Map<String, SystemCheckResultDTO> getResults() {
        return this.results;
    }

    @ApiModel
    public static class SystemCheckResultDTO {
        private final boolean healthy;
        private final String message;

        public SystemCheckResultDTO(boolean healthy, String message) {
            this.healthy = healthy;
            this.message = message;
        }

        @ApiModelProperty(value="Whether the system check succeeded of failed")
        public boolean isHealthy() {
            return this.healthy;
        }

        @ApiModelProperty(value="A description of the success or failure")
        @Nullable
        public String getMessage() {
            return this.message;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.common.ComponentSupport
 */
package org.sonatype.nexus.coreui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.coreui.AssetAttributeTransformer;
import org.sonatype.nexus.coreui.AssetXO;

@Named(value="conan")
@Singleton
public class ConanAttributeTransformer
extends ComponentSupport
implements AssetAttributeTransformer {
    static final String CONAN_FORMAT = "conan";
    static final String INFO_BINARY_ATTRIBUTE = "infoBinary";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_STRING_OBJECT = new TypeReference<Map<String, Object>>(){};

    @Override
    public void transform(AssetXO assetXO) {
        Map<String, Object> formatAttributes = assetXO.getAttributes().getOrDefault(assetXO.getFormat(), Collections.emptyMap());
        if (formatAttributes.containsKey(INFO_BINARY_ATTRIBUTE)) {
            this.updateFormatAttributesMap(formatAttributes);
        }
    }

    private void updateFormatAttributesMap(Map<String, Object> formatAttributesMap) {
        String json = formatAttributesMap.get(INFO_BINARY_ATTRIBUTE).toString();
        try {
            formatAttributesMap.putAll(this.getBinaryInfoAsMap(json));
            formatAttributesMap.remove(INFO_BINARY_ATTRIBUTE);
        }
        catch (IllegalStateException e) {
            this.log.debug("Failed to parse {} as json", (Object)json);
        }
    }

    private Map<String, Object> getBinaryInfoAsMap(String json) {
        Map<String, Object> binaryInfo = this.deserialize(json, MAP_STRING_OBJECT);
        return this.flattenMap(binaryInfo, null);
    }

    private Map<String, Object> flattenMap(Map<String, Object> map, String parentKey) {
        HashMap<String, Object> flattenedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key;
            String string = key = parentKey == null ? entry.getKey() : parentKey + "." + entry.getKey();
            if (entry.getValue() instanceof Map) {
                flattenedMap.putAll(this.flattenMap((Map)entry.getValue(), key));
                continue;
            }
            flattenedMap.put(key, entry.getValue());
        }
        return flattenedMap;
    }

    private <T> T deserialize(String json, TypeReference<T> type) {
        try {
            return (T)OBJECT_MAPPER.readValue(json, type);
        }
        catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}


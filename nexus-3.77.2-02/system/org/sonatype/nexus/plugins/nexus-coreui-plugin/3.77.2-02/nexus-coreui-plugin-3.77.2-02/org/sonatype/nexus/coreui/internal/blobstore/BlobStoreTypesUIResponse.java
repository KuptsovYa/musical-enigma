/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.sonatype.nexus.blobstore.BlobStoreDescriptor
 *  org.sonatype.nexus.blobstore.SelectOption
 *  org.sonatype.nexus.formfields.FormField
 */
package org.sonatype.nexus.coreui.internal.blobstore;

import java.util.List;
import java.util.Map;
import org.sonatype.nexus.blobstore.BlobStoreDescriptor;
import org.sonatype.nexus.blobstore.SelectOption;
import org.sonatype.nexus.formfields.FormField;

public class BlobStoreTypesUIResponse {
    private final String id;
    private final String name;
    private final List<FormField> fields;
    private final String customSettingsForm;
    private final Map<String, List<SelectOption>> dropDownValues;

    public BlobStoreTypesUIResponse(Map.Entry<String, BlobStoreDescriptor> entry) {
        BlobStoreDescriptor descriptor = entry.getValue();
        this.id = descriptor.getId();
        this.name = descriptor.getName();
        this.fields = descriptor.getFormFields();
        this.customSettingsForm = descriptor.customFormName();
        this.dropDownValues = descriptor.getDropDownValues();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<FormField> getFields() {
        return this.fields;
    }

    public String getCustomSettingsForm() {
        return this.customSettingsForm;
    }

    public Map<String, List<SelectOption>> getDropDownValues() {
        return this.dropDownValues;
    }
}


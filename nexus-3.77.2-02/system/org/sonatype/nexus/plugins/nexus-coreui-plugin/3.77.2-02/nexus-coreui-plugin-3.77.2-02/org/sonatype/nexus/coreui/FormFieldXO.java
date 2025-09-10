/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  javax.annotation.Nullable
 *  org.sonatype.nexus.formfields.FormField
 *  org.sonatype.nexus.formfields.NumberTextFormField
 *  org.sonatype.nexus.formfields.Selectable
 */
package org.sonatype.nexus.coreui;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.NumberTextFormField;
import org.sonatype.nexus.formfields.Selectable;

public class FormFieldXO {
    private String id;
    private String type;
    private String label;
    private String helpText;
    private Boolean required;
    private Boolean disabled;
    private Boolean readOnly;
    @Nullable
    private String regexValidation;
    @Nullable
    private String initialValue;
    private Map<String, Object> attributes;
    @Nullable
    private String minValue;
    @Nullable
    private String maxValue;
    @Nullable
    private String storeApi;
    @Nullable
    private Map<String, String> storeFilters;
    @Nullable
    private String idMapping;
    @Nullable
    private String nameMapping;
    private boolean allowAutocomplete;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public Boolean getRequired() {
        return this.required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getRegexValidation() {
        return this.regexValidation;
    }

    public void setRegexValidation(String regexValidation) {
        this.regexValidation = regexValidation;
    }

    public String getInitialValue() {
        return this.initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getMinValue() {
        return this.minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getStoreApi() {
        return this.storeApi;
    }

    public void setStoreApi(String storeApi) {
        this.storeApi = storeApi;
    }

    public Map<String, String> getStoreFilters() {
        return this.storeFilters;
    }

    public void setStoreFilters(Map<String, String> storeFilters) {
        this.storeFilters = storeFilters;
    }

    public String getIdMapping() {
        return this.idMapping;
    }

    public void setIdMapping(String idMapping) {
        this.idMapping = idMapping;
    }

    public String getNameMapping() {
        return this.nameMapping;
    }

    public void setNameMapping(String nameMapping) {
        this.nameMapping = nameMapping;
    }

    public boolean isAllowAutocomplete() {
        return this.allowAutocomplete;
    }

    public void setAllowAutocomplete(boolean allowAutocomplete) {
        this.allowAutocomplete = allowAutocomplete;
    }

    public static FormFieldXO create(FormField<?> source) {
        Preconditions.checkNotNull(source);
        FormFieldXO result = new FormFieldXO();
        result.setId(source.getId());
        result.setType(source.getType());
        result.setLabel(source.getLabel());
        result.setHelpText(source.getHelpText());
        result.setRequired(source.isRequired());
        result.setDisabled(source.isDisabled());
        result.setReadOnly(source.isReadOnly());
        result.setRegexValidation(source.getRegexValidation());
        result.setInitialValue(Optional.ofNullable(source.getInitialValue()).map(Objects::toString).orElse(null));
        result.setAttributes(source.getAttributes());
        if (source instanceof NumberTextFormField) {
            NumberTextFormField ntf = (NumberTextFormField)source;
            result.setMinValue(Optional.ofNullable(ntf.getMinimumValue()).map(Object::toString).orElse(null));
            result.setMaxValue(Optional.ofNullable(ntf.getMaximumValue()).map(Object::toString).orElse(null));
        }
        if (source instanceof Selectable) {
            Selectable selectable = (Selectable)source;
            result.setStoreApi(selectable.getStoreApi());
            result.setStoreFilters(selectable.getStoreFilters());
            result.setAllowAutocomplete(source.getAllowAutocomplete());
            result.setIdMapping(selectable.getIdMapping());
            result.setNameMapping(selectable.getNameMapping());
        }
        return result;
    }

    public String toString() {
        return "FormFieldXO{id='" + this.id + "', type='" + this.type + "', label='" + this.label + "', helpText='" + this.helpText + "', required=" + this.required + ", disabled=" + this.disabled + ", readOnly=" + this.readOnly + ", regexValidation='" + this.regexValidation + "', initialValue='" + this.initialValue + "', attributes=" + this.attributes + ", minValue='" + this.minValue + "', maxValue='" + this.maxValue + "', storeApi='" + this.storeApi + "', storeFilters=" + this.storeFilters + ", idMapping='" + this.idMapping + "', nameMapping='" + this.nameMapping + "', allowAutocomplete=" + this.allowAutocomplete + "}";
    }
}


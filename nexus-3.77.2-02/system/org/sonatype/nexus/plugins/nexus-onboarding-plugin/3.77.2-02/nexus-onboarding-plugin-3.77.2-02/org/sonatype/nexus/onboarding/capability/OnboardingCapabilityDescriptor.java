/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  org.sonatype.goodies.i18n.I18N
 *  org.sonatype.goodies.i18n.MessageBundle
 *  org.sonatype.goodies.i18n.MessageBundle$DefaultMessage
 *  org.sonatype.nexus.capability.CapabilityDescriptorSupport
 *  org.sonatype.nexus.capability.CapabilityType
 *  org.sonatype.nexus.capability.Tag
 *  org.sonatype.nexus.capability.Taggable
 *  org.sonatype.nexus.common.upgrade.AvailabilityVersion
 *  org.sonatype.nexus.formfields.FormField
 */
package org.sonatype.nexus.onboarding.capability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.i18n.I18N;
import org.sonatype.goodies.i18n.MessageBundle;
import org.sonatype.nexus.capability.CapabilityDescriptorSupport;
import org.sonatype.nexus.capability.CapabilityType;
import org.sonatype.nexus.capability.Tag;
import org.sonatype.nexus.capability.Taggable;
import org.sonatype.nexus.common.upgrade.AvailabilityVersion;
import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.onboarding.capability.OnboardingCapability;
import org.sonatype.nexus.onboarding.capability.OnboardingCapabilityConfiguration;

@AvailabilityVersion(from="1.0")
@Named(value="onboarding-wizard")
@Singleton
public class OnboardingCapabilityDescriptor
extends CapabilityDescriptorSupport<OnboardingCapabilityConfiguration>
implements Taggable {
    static final Messages messages = (Messages)I18N.create(Messages.class);

    public OnboardingCapabilityDescriptor() {
        this.setExposed(false);
        this.setHidden(true);
    }

    public CapabilityType type() {
        return OnboardingCapability.TYPE;
    }

    public String name() {
        return messages.name();
    }

    public List<FormField> formFields() {
        return new ArrayList<FormField>();
    }

    public Set<Tag> getTags() {
        return new HashSet<Tag>(Collections.singletonList(Tag.categoryTag((String)messages.category())));
    }

    static interface Messages
    extends MessageBundle {
        @MessageBundle.DefaultMessage(value="Onboarding wizard")
        public String name();

        @MessageBundle.DefaultMessage(value="Onboarding")
        public String category();

        @MessageBundle.DefaultMessage(value="Enabled")
        public String enabled();

        @MessageBundle.DefaultMessage(value="Disabled")
        public String disabled();
    }
}


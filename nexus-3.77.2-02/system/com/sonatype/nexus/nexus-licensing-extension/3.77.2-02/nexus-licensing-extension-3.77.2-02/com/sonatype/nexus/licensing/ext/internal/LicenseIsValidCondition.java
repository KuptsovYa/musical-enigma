/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.eventbus.Subscribe
 *  org.sonatype.goodies.i18n.I18N
 *  org.sonatype.goodies.i18n.MessageBundle
 *  org.sonatype.goodies.i18n.MessageBundle$DefaultMessage
 *  org.sonatype.licensing.feature.Feature
 *  org.sonatype.licensing.feature.LicenseFeatureVerifier
 *  org.sonatype.nexus.capability.Condition
 *  org.sonatype.nexus.capability.condition.ConditionSupport
 *  org.sonatype.nexus.common.event.EventManager
 */
package com.sonatype.nexus.licensing.ext.internal;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.sonatype.nexus.licensing.ext.LicenseChangedEvent;
import org.sonatype.goodies.i18n.I18N;
import org.sonatype.goodies.i18n.MessageBundle;
import org.sonatype.licensing.feature.Feature;
import org.sonatype.licensing.feature.LicenseFeatureVerifier;
import org.sonatype.nexus.capability.Condition;
import org.sonatype.nexus.capability.condition.ConditionSupport;
import org.sonatype.nexus.common.event.EventManager;

public class LicenseIsValidCondition
extends ConditionSupport
implements Condition {
    private static final Messages messages = (Messages)I18N.create(Messages.class);
    private final LicenseFeatureVerifier verifier;
    private final Feature feature;

    LicenseIsValidCondition(EventManager eventManager, LicenseFeatureVerifier verifier, Feature feature) {
        super(eventManager, false);
        this.verifier = (LicenseFeatureVerifier)Preconditions.checkNotNull((Object)verifier);
        this.feature = (Feature)Preconditions.checkNotNull((Object)feature);
    }

    @Subscribe
    public void handle(LicenseChangedEvent event) {
        this.log.debug("License changed: {}", (Object)event);
        this.verify();
    }

    protected void doBind() {
        this.getEventManager().register((Object)this);
        this.verify();
    }

    protected void doRelease() {
        this.getEventManager().unregister((Object)this);
    }

    public String toString() {
        return this.explainSatisfied();
    }

    public String explainSatisfied() {
        return messages.satisfied(this.feature.getName());
    }

    public String explainUnsatisfied() {
        return messages.unsatisfied(this.feature.getName());
    }

    private void verify() {
        boolean valid = this.verifier.verify(this.feature);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Feature '{}' is {}", (Object)this.feature.getId(), (Object)(valid ? "valid" : "invalid"));
        }
        this.setSatisfied(valid);
    }

    private static interface Messages
    extends MessageBundle {
        @MessageBundle.DefaultMessage(value="License permits use of feature '%s'")
        public String satisfied(String var1);

        @MessageBundle.DefaultMessage(value="License does not permit use of feature '%s'")
        public String unsatisfied(String var1);
    }
}


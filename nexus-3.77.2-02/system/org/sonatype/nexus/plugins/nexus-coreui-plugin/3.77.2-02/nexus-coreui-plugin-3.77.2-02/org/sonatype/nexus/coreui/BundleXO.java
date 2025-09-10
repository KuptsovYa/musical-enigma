/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotBlank
 */
package org.sonatype.nexus.coreui;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class BundleXO {
    @Min(value=0L)
    private @Min(value=0L) long id;
    @NotBlank
    private String state;
    @NotBlank
    private String name;
    @NotBlank
    private String symbolicName;
    @NotBlank
    private String location;
    @NotBlank
    private String version;
    @Min(value=0L)
    private @Min(value=0L) int startLevel;
    private boolean fragment;
    private long lastModified;
    private List<Long> fragments;
    private List<Long> fragmentHosts;
    private Map<String, String> headers;

    public long getId() {
        return this.id;
    }

    public BundleXO withId(long id) {
        this.id = id;
        return this;
    }

    public String getState() {
        return this.state;
    }

    public BundleXO withState(String state) {
        this.state = state;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public BundleXO withName(String name) {
        this.name = name;
        return this;
    }

    public String getSymbolicName() {
        return this.symbolicName;
    }

    public BundleXO withSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
        return this;
    }

    public String getLocation() {
        return this.location;
    }

    public BundleXO withLocation(String location) {
        this.location = location;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public BundleXO withVersion(String version) {
        this.version = version;
        return this;
    }

    public int getStartLevel() {
        return this.startLevel;
    }

    public BundleXO withStartLevel(int startLevel) {
        this.startLevel = startLevel;
        return this;
    }

    public boolean isFragment() {
        return this.fragment;
    }

    public BundleXO withFragment(boolean fragment) {
        this.fragment = fragment;
        return this;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public BundleXO withLastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public List<Long> getFragments() {
        return this.fragments;
    }

    public BundleXO withFragments(List<Long> fragments) {
        this.fragments = fragments;
        return this;
    }

    public List<Long> getFragmentHosts() {
        return this.fragmentHosts;
    }

    public BundleXO withFragmentHosts(List<Long> fragmentHosts) {
        this.fragmentHosts = fragmentHosts;
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public BundleXO withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}


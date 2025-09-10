/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.codahale.metrics.annotation.ExceptionMetered
 *  com.codahale.metrics.annotation.Timed
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Preconditions
 *  com.softwarementors.extjs.djn.config.annotations.DirectAction
 *  com.softwarementors.extjs.djn.config.annotations.DirectMethod
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  javax.validation.ValidationException
 *  org.apache.shiro.authz.annotation.RequiresPermissions
 *  org.sonatype.nexus.common.event.EventManager
 *  org.sonatype.nexus.extdirect.DirectComponentSupport
 *  org.sonatype.nexus.extdirect.model.LimitedPagedResponse
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters$Filter
 *  org.sonatype.nexus.extdirect.model.StoreLoadParameters$Sort
 *  org.sonatype.nexus.repository.search.ComponentSearchResult
 *  org.sonatype.nexus.repository.search.SearchRequest
 *  org.sonatype.nexus.repository.search.SearchResponse
 *  org.sonatype.nexus.repository.search.SearchService
 *  org.sonatype.nexus.repository.search.SortDirection
 *  org.sonatype.nexus.repository.search.event.SearchEvent
 *  org.sonatype.nexus.repository.search.event.SearchEventSource
 *  org.sonatype.nexus.repository.search.query.SearchFilter
 *  org.sonatype.nexus.repository.search.query.SearchResultsGenerator
 *  org.sonatype.nexus.rest.ValidationErrorsException
 */
package org.sonatype.nexus.coreui;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.softwarementors.extjs.djn.config.annotations.DirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.ValidationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.coreui.ComponentXO;
import org.sonatype.nexus.extdirect.DirectComponentSupport;
import org.sonatype.nexus.extdirect.model.LimitedPagedResponse;
import org.sonatype.nexus.extdirect.model.StoreLoadParameters;
import org.sonatype.nexus.repository.search.ComponentSearchResult;
import org.sonatype.nexus.repository.search.SearchRequest;
import org.sonatype.nexus.repository.search.SearchResponse;
import org.sonatype.nexus.repository.search.SearchService;
import org.sonatype.nexus.repository.search.SortDirection;
import org.sonatype.nexus.repository.search.event.SearchEvent;
import org.sonatype.nexus.repository.search.event.SearchEventSource;
import org.sonatype.nexus.repository.search.query.SearchFilter;
import org.sonatype.nexus.repository.search.query.SearchResultsGenerator;
import org.sonatype.nexus.rest.ValidationErrorsException;

@Named
@Singleton
@DirectAction(action={"coreui_Search"})
public class SearchComponent
extends DirectComponentSupport {
    private final SearchService searchService;
    private final EventManager eventManager;
    private final SearchResultsGenerator searchResultsGenerator;
    private int searchResultsLimit;

    @Inject
    public SearchComponent(SearchService searchService, @Named(value="${nexus.searchResultsLimit:-1000}") int searchResultsLimit, SearchResultsGenerator searchResultsGenerator, EventManager eventManager) {
        this.searchService = (SearchService)Preconditions.checkNotNull((Object)searchService);
        this.searchResultsLimit = searchResultsLimit;
        this.searchResultsGenerator = (SearchResultsGenerator)Preconditions.checkNotNull((Object)searchResultsGenerator);
        this.eventManager = (EventManager)Preconditions.checkNotNull((Object)eventManager);
    }

    @DirectMethod
    @Timed
    @ExceptionMetered
    @RequiresPermissions(value={"nexus:search:read"})
    public LimitedPagedResponse<ComponentXO> read(StoreLoadParameters parameters) {
        boolean formatCriteriaOnly;
        List<SearchFilter> searchFilters = SearchComponent.createFilters(parameters.getFilters());
        boolean bl = formatCriteriaOnly = searchFilters.size() == 1 && "format".equals(searchFilters.get(0).getProperty());
        if (searchFilters.size() == 0 || parameters.isFormatSearch() && formatCriteriaOnly) {
            return new LimitedPagedResponse((long)parameters.getLimit().intValue(), 0L, Collections.emptyList());
        }
        if (formatCriteriaOnly) {
            throw new ValidationErrorsException("Specify at least one more search criteria with the format");
        }
        this.fireSearchEvent(searchFilters);
        try {
            return this.componentSearch(parameters.getLimit(), parameters.getPage(), SearchComponent.orEmpty(parameters.getSort()), searchFilters);
        }
        catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private static List<SearchFilter> createFilters(List<StoreLoadParameters.Filter> filters) {
        return Optional.ofNullable(filters).map(Collection::stream).orElseGet(Stream::empty).map(filter -> new SearchFilter(filter.getProperty(), filter.getValue())).collect(Collectors.toList());
    }

    private LimitedPagedResponse<ComponentXO> componentSearch(Integer limit, Integer page, List<StoreLoadParameters.Sort> sort, List<SearchFilter> filters) {
        String sortField = sort.stream().findFirst().map(StoreLoadParameters.Sort::getProperty).orElse(null);
        String sortDirection = sort.stream().findFirst().map(StoreLoadParameters.Sort::getDirection).orElse(null);
        int queryLimit = Math.min(limit, this.searchResultsLimit);
        int offset = Optional.ofNullable(page).map(p -> p - 1).map(p -> p * queryLimit).orElse(0);
        SearchRequest request = SearchRequest.builder().searchFilters(filters).sortField(sortField).limit(Integer.valueOf(queryLimit)).offset(Integer.valueOf(offset)).sortDirection((SortDirection)Optional.ofNullable(sortDirection).map(String::toUpperCase).map(SortDirection::valueOf).orElse(null)).build();
        this.log.debug("UI Search Query {}", (Object)request);
        SearchResponse response = this.searchService.search(request);
        List componentXOs = this.searchResultsGenerator.getSearchResultList(response).stream().map(SearchComponent::toComponent).collect(Collectors.toList());
        return new LimitedPagedResponse((long)limit.intValue(), response.getTotalHits().longValue(), componentXOs, false);
    }

    @VisibleForTesting
    public int getSearchResultsLimit() {
        return this.searchResultsLimit;
    }

    @VisibleForTesting
    public void setSearchResultsLimit(int searchResultsLimit) {
        this.searchResultsLimit = searchResultsLimit;
    }

    private static ComponentXO toComponent(ComponentSearchResult componentHit) {
        ComponentXO componentXO = new ComponentXO();
        componentXO.setGroup(componentHit.getGroup());
        componentXO.setName(componentHit.getName());
        componentXO.setVersion(componentHit.getVersion());
        componentXO.setId(componentHit.getId());
        componentXO.setRepositoryName(componentHit.getRepositoryName());
        componentXO.setFormat(componentHit.getFormat());
        if (componentHit.getLastModified() != null) {
            componentXO.setLastBlobUpdated(componentHit.getLastModified().toString());
        }
        return componentXO;
    }

    private void fireSearchEvent(Collection<SearchFilter> searchFilters) {
        this.eventManager.post((Object)new SearchEvent(searchFilters, SearchEventSource.UI));
    }

    private static List<StoreLoadParameters.Sort> orEmpty(List<StoreLoadParameters.Sort> sort) {
        return sort != null ? sort : Collections.emptyList();
    }
}


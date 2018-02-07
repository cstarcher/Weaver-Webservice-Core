/* 
 * WebSocketRequestMappingHandler.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.weaver.mapping.condition;

/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.MessageCondition;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class WebSocketRequestCondition implements MessageCondition<WebSocketRequestCondition> {

    private final Set<String> patterns;

    private final PathMatcher pathMatcher;

    public WebSocketRequestCondition(String... patterns) {
        this(Arrays.asList(patterns), null);
    }

    public WebSocketRequestCondition(String[] patterns, PathMatcher pathMatcher) {
        this(Arrays.asList(patterns), pathMatcher);
    }

    public WebSocketRequestCondition(Collection<String> patterns, PathMatcher pathMatcher) {
        this.patterns = Collections.unmodifiableSet(new HashSet<String>(patterns));
        this.pathMatcher = (pathMatcher != null ? pathMatcher : (PathMatcher) new AntPathMatcher());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSocketRequestCondition combine(WebSocketRequestCondition other) {
        Set<String> result = new LinkedHashSet<String>();
        if (!this.patterns.isEmpty() && !other.patterns.isEmpty()) {
            for (String pattern1 : this.patterns) {
                for (String pattern2 : other.patterns) {
                    result.add(((AntPathMatcher) this.pathMatcher).combine(pattern1, pattern2));
                }
            }
        } else if (!this.patterns.isEmpty()) {
            result.addAll(this.patterns);
        } else if (!other.patterns.isEmpty()) {
            result.addAll(other.patterns);
        } else {
            result.add("");
        }
        return new WebSocketRequestCondition(result, this.pathMatcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(WebSocketRequestCondition other, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        Comparator<String> patternComparator = ((AntPathMatcher) this.pathMatcher).getPatternComparator(destination);

        Iterator<String> iterator = patterns.iterator();
        Iterator<String> iteratorOther = other.patterns.iterator();
        while (iterator.hasNext() && iteratorOther.hasNext()) {
            int result = patternComparator.compare(iterator.next(), iteratorOther.next());
            if (result != 0) {
                return result;
            }
        }
        if (iterator.hasNext()) {
            return -1;
        } else if (iteratorOther.hasNext()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSocketRequestCondition getMatchingCondition(Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();

        if (destination == null) {
            return null;
        }

        if (this.patterns.isEmpty()) {
            return this;
        }

        List<String> matches = new ArrayList<String>();
        for (String pattern : patterns) {

            if (("/ws" + pattern).equals(destination)) {
                matches.add("/ws" + pattern);
            }

            if (("/channel" + pattern).equals(destination)) {
                matches.add("/channel" + pattern);
            }

            if (("/private/queue" + pattern).equals(destination)) {
                matches.add("/private/queue" + pattern);
            }

            if (((AntPathMatcher) this.pathMatcher).match(("/ws" + pattern), destination)) {
                matches.add("/ws" + pattern);
            }

            if (((AntPathMatcher) this.pathMatcher).match(("/channel" + pattern), destination)) {
                matches.add("/channel" + pattern);
            }

            if (((AntPathMatcher) this.pathMatcher).match(("/private/queue" + pattern), destination)) {
                matches.add("/private/queue" + pattern);
            }
        }

        if (matches.isEmpty()) {
            return null;
        }

        Collections.sort(matches, ((AntPathMatcher) this.pathMatcher).getPatternComparator(destination));

        return new WebSocketRequestCondition(matches, this.pathMatcher);
    }

    public Set<String> getPatterns() {
        return patterns;
    }

}
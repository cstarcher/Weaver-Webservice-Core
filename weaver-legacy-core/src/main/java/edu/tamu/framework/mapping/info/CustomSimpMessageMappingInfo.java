package edu.tamu.weaver.mapping.info;

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

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.MessageCondition;
import org.springframework.messaging.simp.SimpMessageTypeMessageCondition;

import edu.tamu.weaver.mapping.condition.WebSocketRequestCondition;

/**
 * Custom simp message mapping info. Mostly duplication of Spring's
 * SimpMessageMappingInfo.
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
public class CustomSimpMessageMappingInfo implements MessageCondition<CustomSimpMessageMappingInfo> {

    private final SimpMessageTypeMessageCondition messageTypeMessageCondition;

    private final WebSocketRequestCondition destinationConditions;

    public CustomSimpMessageMappingInfo(SimpMessageTypeMessageCondition messageTypeMessageCondition, WebSocketRequestCondition destinationConditions) {
        this.messageTypeMessageCondition = messageTypeMessageCondition;
        this.destinationConditions = destinationConditions;
    }

    public SimpMessageTypeMessageCondition getMessageTypeMessageCondition() {
        return this.messageTypeMessageCondition;
    }

    public WebSocketRequestCondition getDestinationConditions() {
        return this.destinationConditions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomSimpMessageMappingInfo combine(CustomSimpMessageMappingInfo other) {
        SimpMessageTypeMessageCondition typeCond = this.getMessageTypeMessageCondition().combine(other.getMessageTypeMessageCondition());
        WebSocketRequestCondition destCond = this.destinationConditions.combine(other.getDestinationConditions());
        return new CustomSimpMessageMappingInfo(typeCond, destCond);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomSimpMessageMappingInfo getMatchingCondition(Message<?> message) {
        SimpMessageTypeMessageCondition typeCond = this.messageTypeMessageCondition.getMatchingCondition(message);
        if (typeCond == null) {
            return null;
        }
        WebSocketRequestCondition destCond = this.destinationConditions.getMatchingCondition(message);
        if (destCond == null) {
            return null;
        }
        return new CustomSimpMessageMappingInfo(typeCond, destCond);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(CustomSimpMessageMappingInfo other, Message<?> message) {
        int result = this.messageTypeMessageCondition.compareTo(other.messageTypeMessageCondition, message);
        if (result != 0) {
            return result;
        }
        result = this.destinationConditions.compareTo(other.destinationConditions, message);
        if (result != 0) {
            return result;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof CustomSimpMessageMappingInfo) {
            CustomSimpMessageMappingInfo other = (CustomSimpMessageMappingInfo) obj;
            return (this.destinationConditions.equals(other.destinationConditions) && this.messageTypeMessageCondition.equals(other.messageTypeMessageCondition));
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (this.destinationConditions.hashCode() * 31 + this.messageTypeMessageCondition.hashCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append(this.destinationConditions);
        builder.append(",messageType=").append(this.messageTypeMessageCondition);
        builder.append('}');
        return builder.toString();
    }

}
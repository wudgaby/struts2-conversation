/*******************************************************************************
 * 
 *  Struts2-Conversation-Plugin - An Open Source Conversation- and Flow-Scope Solution for Struts2-based Applications
 *  =================================================================================================================
 * 
 *  Copyright (C) 2012 by Rees Byars
 *  http://code.google.com/p/struts2-conversation/
 * 
 * **********************************************************************************************************************
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations under the License.
 * 
 * **********************************************************************************************************************
 * 
 *  $Id: DefaultConversationConfigurationProvider.java reesbyars $
 ******************************************************************************/
package com.google.code.rees.scope.conversation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.rees.scope.util.ReflectionUtil;

/**
 * The default implementation of {@link ConversationConfigurationProvider}
 * 
 * @author rees.byars
 */
public class DefaultConversationConfigurationProvider implements
        ConversationConfigurationProvider {

    private static final long serialVersionUID = -1227350994518195549L;
    private static final Logger LOG = LoggerFactory
            .getLogger(DefaultConversationConfigurationProvider.class);

    protected ConversationArbitrator arbitrator = new DefaultConversationArbitrator();
    protected transient Map<Class<?>, Collection<ConversationConfiguration>> classConfigurations = Collections
            .synchronizedMap(new HashMap<Class<?>, Collection<ConversationConfiguration>>());

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArbitrator(ConversationArbitrator arbitrator) {
        this.arbitrator = arbitrator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Set<Class<?>> actionClasses) {
        for (Class<?> clazz : actionClasses) {
            processClass(clazz, classConfigurations);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ConversationConfiguration> getConfigurations(
            Class<?> clazz) {

        if (this.classConfigurations == null) {
            this.classConfigurations = Collections
                    .synchronizedMap(new HashMap<Class<?>, Collection<ConversationConfiguration>>());
        }
        Collection<ConversationConfiguration> configurations = classConfigurations
                .get(clazz);
        if (configurations == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No cached ConversationConfiguration found for class "
                        + clazz.getName());
            }
            configurations = this.processClass(clazz, classConfigurations);
        }
        return configurations;
    }

    /**
     * good candidate for refactoring... but it works!
     * 
     * @param clazz
     * @param classConfigurations
     * @return
     */
    protected synchronized Collection<ConversationConfiguration> processClass(
            Class<?> clazz,
            Map<Class<?>, Collection<ConversationConfiguration>> classConfigurations) {
        Collection<ConversationConfiguration> configurations = classConfigurations
                .get(clazz);
        if (configurations == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Building ConversationConfiguration for class "
                        + clazz.getName());
            }
            configurations = new HashSet<ConversationConfiguration>();
            Map<String, ConversationConfiguration> temporaryConversationMap = new HashMap<String, ConversationConfiguration>();
            for (Field field : this.arbitrator
                    .getCandidateConversationFields(clazz)) {
                Collection<String> fieldConversations = this.arbitrator
                        .getConversations(clazz, field);
                if (fieldConversations != null) {
                    String fieldName = this.arbitrator.getName(field);
                    ReflectionUtil.makeAccessible(field);
                    for (String conversation : fieldConversations) {
                        ConversationConfiguration configuration = temporaryConversationMap
                                .get(conversation);
                        if (configuration == null) {
                            configuration = new ConversationConfiguration(
                                    conversation);
                            temporaryConversationMap.put(conversation,
                                    configuration);
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding field "
                                    + fieldName
                                    + " to ConversationConfiguration for Conversation "
                                    + conversation);
                        }
                        configuration.addField(fieldName, field);
                    }
                }
            }
            // TODO refactor into multiple methods to make more beautimous
            for (Method method : this.arbitrator
                    .getCandidateConversationMethods(clazz)) {
                Collection<String> methodConversations = this.arbitrator
                        .getConversations(clazz, method);
                if (methodConversations != null) {
                    String methodName = this.arbitrator.getName(method);
                    for (String conversation : methodConversations) {
                        ConversationConfiguration configuration = temporaryConversationMap
                                .get(conversation);
                        if (configuration == null) {
                            configuration = new ConversationConfiguration(
                                    conversation);
                            temporaryConversationMap.put(conversation,
                                    configuration);
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding method "
                                    + methodName
                                    + " as an Action to ConversationConfiguration for Conversation "
                                    + conversation);
                        }
                        configuration.addAction(methodName);
                    }
                }
                Collection<String> methodBeginConversations = this.arbitrator
                        .getBeginConversations(clazz, method);
                if (methodBeginConversations != null) {
                    String methodName = this.arbitrator.getName(method);
                    for (String conversation : methodBeginConversations) {
                        ConversationConfiguration configuration = temporaryConversationMap
                                .get(conversation);
                        if (configuration == null) {
                            configuration = new ConversationConfiguration(
                                    conversation);
                            temporaryConversationMap.put(conversation,
                                    configuration);
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding method "
                                    + methodName
                                    + " as a Begin Action to ConversationConfiguration for Conversation "
                                    + conversation);
                        }
                        configuration.addBeginAction(methodName);
                    }
                }
                Collection<String> methodEndConversations = this.arbitrator
                        .getEndConversations(clazz, method);
                if (methodEndConversations != null) {
                    String methodName = this.arbitrator.getName(method);
                    for (String conversation : methodEndConversations) {
                        ConversationConfiguration configuration = temporaryConversationMap
                                .get(conversation);
                        if (configuration == null) {
                            configuration = new ConversationConfiguration(
                                    conversation);
                            temporaryConversationMap.put(conversation,
                                    configuration);
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding method "
                                    + methodName
                                    + " as an End Action to ConversationConfiguration for Conversation "
                                    + conversation);
                        }
                        configuration.addEndAction(methodName);
                    }
                }
            }
            configurations.addAll(temporaryConversationMap.values());
            classConfigurations.put(clazz, configurations);
        }
        return configurations;
    }

}
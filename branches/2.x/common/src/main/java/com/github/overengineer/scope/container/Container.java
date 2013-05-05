package com.github.overengineer.scope.container;

import java.util.List;

/**
 * @author rees.byars
 */
public interface Container extends Provider {

    void verify() throws WiringException;

    Container loadModule(Class<? extends Module> module);

    Container addCascadingContainer(Container container);

    Container addChild(Container container);

    Container addListener(Class<? extends ComponentInitializationListener> listenerClass);

    <T> Container add(Class<T> componentType, Class<? extends T> implementationType);

    <T> Container add(Key key, Class<? extends T> implementationType);

    <T, I extends T> Container addInstance(Class<T> componentType, I implementation);

    <T, I extends T> Container addInstance(Key key, I implementation);

    Container addProperty(String propertyName, Object propertyValue);

    List<Object> getAllComponents();

    List<Container> getCascadingContainers();

    List<Container> getChildren();

    Container getReal();

}
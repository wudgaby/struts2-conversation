package com.github.overengineer.container.proxy.aop;

import com.github.overengineer.container.*;
import com.github.overengineer.container.dynamic.DynamicComponentFactory;
import com.github.overengineer.container.proxy.DefaultHotSwappableContainer;
import com.github.overengineer.container.key.KeyRepository;

import java.util.List;

/**
 * @author rees.byars
 */
public class DefaultAopContainer extends DefaultHotSwappableContainer implements AopContainer {

    private final ComponentStrategyFactory strategyFactory;
    private final List<Aspect> aspects;

    public DefaultAopContainer(ComponentStrategyFactory strategyFactory, KeyRepository keyRepository, DynamicComponentFactory dynamicComponentFactory, List<ComponentInitializationListener> componentInitializationListeners, List<Aspect> aspects) {
        super(strategyFactory, keyRepository, dynamicComponentFactory, componentInitializationListeners);
        this.strategyFactory = strategyFactory;
        this.aspects = aspects;
    }

    @Override
    public <A extends Aspect<?>> AopContainer addAspect(Class<A> interceptorClass) {
        ComponentStrategy<A> strategy = strategyFactory.create(interceptorClass);
        Aspect aspect = strategy.get(this);
        getAspects().add(strategy.get(this));
        for (Container container : getChildren()) {
            if (container instanceof AopContainer) {
                ((AopContainer) container).getAspects().add(aspect);
            }
        }
        return get(AopContainer.class);
    }

    @Override
    public List<Aspect> getAspects() {
        return aspects;
    }

    @Override
    public Container makeInjectable() {
        addInstance(AopContainer.class, this);
        return super.makeInjectable();
    }

}

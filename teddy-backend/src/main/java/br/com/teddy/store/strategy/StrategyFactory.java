package br.com.teddy.store.strategy;

import br.com.teddy.store.domain.EnumOperation;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StrategyFactory {

    private ApplicationContext context;

    public StrategyFactory(ApplicationContext context){
        this.context = context;
    }

    public List<IStrategy> getRules(Class clazz, EnumOperation enumOperation) {
        Reflections reflections = new Reflections("br.com.teddy.store.strategy");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(StrategyAnnotation.class);
        List<Class<?>> classList = typesAnnotatedWith.stream().filter(item -> {
            StrategyAnnotation annotation = item.getAnnotation(StrategyAnnotation.class);
            return annotation.operation().equals(enumOperation) && (void.class == annotation.target() || clazz.equals(annotation.target()));
        }).collect(Collectors.toList());

        List<IStrategy> generalList = new ArrayList();

        classList.forEach(item -> {
            IStrategy iStrategy = (IStrategy) this.context.getBean(item);
            generalList.add(iStrategy);
        });

        return generalList;
    }
}

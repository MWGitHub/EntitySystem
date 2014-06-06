package com.exploringlines.entitysystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/17/13
 * Time: 12:22 PM
 * Handles the creation, modification, and removal of entities.
 */
public class EntitySystem {
    /**
     * Counter for the entity id.
     */
    private long counter = 0;

    /**
     * Entities registered with the system.
     */
    private List<Entity> entities = new LinkedList<Entity>();

    /**
     * Maps the component to entity relationship.
     */
    private Map<Entity, Map<Class, Component>> entityComponentMap = new HashMap<Entity, Map<Class, Component>>();

    /**
     * Maps the component to entity relationship for removed entities.
     */
    private Map<Entity, Map<Class, Component>> removedEntityComponentMap = new HashMap<Entity, Map<Class, Component>>();

    /**
     * Stores entity sets.
     * The sets are created on query and cached.
     */
    private Map<Class, EntitySet> entitySets = new HashMap<Class, EntitySet>();

    /**
     * Initializes the system.
     */
    public EntitySystem() {
    }

    /**
     * Creates an entity.
     * @return the created entity.
     */
    public Entity createEntity() {
        Entity entity = new Entity(counter);
        counter++;
        entities.add(entity);
        return entity;
    }

    /**
     * Creates an entity.
     * @param name the name of the entity.
     * @return the created entity.
     */
    public Entity createEntity(String name) {
        Entity entity = new Entity(counter, name);
        counter++;
        entities.add(entity);
        return entity;
    }

    /**
     * Checks if the entity exists in the entity system.
     * @param entity the entity to find.
     * @return true if the entity is in the entity system.
     */
    public boolean hasEntity(Entity entity) {
        return entities.contains(entity);
    }

    /**
     * Retrieves an entity by the id.
     * @param id the id of the entity.
     * @return the entity retrieved, null if none found.
     */
    public Entity getEntity(long id) {
        for (Entity entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Retrieves an entity by name.
     * @param name the name of the entity.
     * @return the entity retrieved, null if none found.
     */
    public Entity getEntity(String name) {
        for (Entity entity : entities) {
            String entityName = entity.getName();
            if (entityName != null && entityName.equals(name)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Removes an entity.
     * @param entity the entity to remove.
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);

        // Move the component map to the removed map.
        Map<Class, Component> componentMap = entityComponentMap.get(entity);
        if (componentMap != null) {
            removedEntityComponentMap.put(entity, componentMap);
        }

        // Notify all sets with the removed entity.
        entityComponentMap.remove(entity);
        for (EntitySet entitySet : entitySets.values()) {
            entitySet.removeEntity(entity);
        }
        entityComponentMap.remove(entity);
    }

    /**
     * Retrieves a component on an entity.
     * @param entity the entity to retrieve the component from.
     * @param componentClass the class of the component.
     * @param <T> the type of component.
     * @return the first found component or null if none given.
     */
    public <T extends Component> T getComponent(Entity entity, Class<T> componentClass) {
        if (entityComponentMap.containsKey(entity)) {
            Map<Class, Component> componentList = entityComponentMap.get(entity);
            return componentClass.cast(componentList.get(componentClass));
        } else if (removedEntityComponentMap.containsKey(entity)) {
            Map<Class, Component> componentList = removedEntityComponentMap.get(entity);
            return componentClass.cast(componentList.get(componentClass));
        }
        return null;
    }

    /**
     * Sets a component to an entity.
     * @param entity the entity to attach the component to.
     * @param component the component to set to the entity.
     * @param <T> the type of component.
     */
    public <T extends Component> T setComponent(Entity entity, T component) {
        if (!entityComponentMap.containsKey(entity)) {
            entityComponentMap.put(entity, new HashMap<Class, Component>());
        }
        Map<Class, Component> components = entityComponentMap.get(entity);
        // Add an entity reference to the entry set which contains the added component.
        if (entitySets.containsKey(component.getClass())) {
            EntitySet set = entitySets.get(component.getClass());
            // Add the entity to the changed list if the component exists and is referenced to the entity but is changed.
            if (components.containsKey(component.getClass())) {
                set.addChangedEntity(entity);
            } else {
                set.addEntity(entity);
            }
        }
        components.put(component.getClass(), component);

        return component;
    }

    /**
     * Checks if an entity has the specified component.
     * @param entity the entity to check.
     * @param componentClass the component to check if the entity contains.
     * @param <T> the class of the component.
     * @return true if the entity has the component.
     */
    public <T extends Component> boolean hasComponent(Entity entity, Class<T> componentClass) {
        return getComponent(entity, componentClass) != null;
    }

    /**
     * Removes a component from an entity.
     * @param entity the entity to remove the component from.
     * @param componentClass the component to remove.
     * @param <T> the type of component.
     */
    public <T extends Component> void removeComponent(Entity entity, Class<T> componentClass) {
        if (entityComponentMap.containsKey(entity)) {
            entityComponentMap.get(entity).remove(componentClass);

            // Move the component map to the removed map.
            Map<Class, Component> componentMap = entityComponentMap.get(entity);
            if (componentMap != null) {
                removedEntityComponentMap.put(entity, componentMap);
            }

            // Remove the entity reference in the entry set which contain the removed component.
            if (entitySets.containsKey(componentClass)) {
                entitySets.get(componentClass).removeEntity(entity);
            }
        }
    }

    /**
     * Retrieves entities that have the given components.
     * @param componentClass the components an entity will contain.
     * @param <T> the type of component.
     * @return the entities that have the given components.
     */
    public <T extends Component> EntitySet getEntities(Class<T> componentClass) {
        if (!entitySets.containsKey(componentClass)) {
            EntitySet entitySet = new EntitySet();
            // Add existing entities that match the component.
            for (Entity entity : entities) {
                if (hasComponent(entity, componentClass)) {
                    entitySet.addEntity(entity);
                }
            }
            entitySets.put(componentClass, entitySet);
        }
        return entitySets.get(componentClass);
    }

    /**
     * Retrieves all components of the given type.
     * @param componentClass the component type to retrieve.
     * @param <T> the type of component.
     * @return the components of type T.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Collection<T> getComponentsOfType(Class<T> componentClass) {
        Collection<T> list = new ArrayList<T>();
        for (Map<Class, Component> type : entityComponentMap.values()) {
            for (Map.Entry<Class, Component> entry : type.entrySet()) {
                if (entry.getKey().equals(componentClass)) {
                    list.addAll((Collection<T>) type.values());
                }
            }
        }
        return list;
    }

    /**
     * Flushes set changes.
     */
    public void flushSetChanges() {
        for (EntitySet set : entitySets.values()) {
            set.flushChanges();
        }
        removedEntityComponentMap.clear();
    }
}

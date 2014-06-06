package com.exploringlines.entitysystem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/17/13
 * Time: 12:37 PM
 * A set of entities with components in common.
 */
public class EntitySet {
    /**
     * Entities in the set.
     */
    private List<Entity> entities = new LinkedList<Entity>();

    /**
     * Holds the entities that were just added.
     */
    private List<Entity> addedEntities = new LinkedList<Entity>();

    /**
     * Holds entities that were changed (newly added entities will not be in here).
     */
    private List<Entity> changedEntities = new LinkedList<Entity>();

    /**
     * Holds the entities that were removed.
     */
    private List<Entity> removedEntities = new LinkedList<Entity>();

    /**
     * Initializes the set.
     */
    public EntitySet() {
    }

    /**
     * Adds an entity to the set.
     * @param entity the entity to add.
     */
    protected void addEntity(Entity entity) {
        if (!entities.contains(entity) && entities.add(entity)) {
            addedEntities.add(entity);
        }
    }

    /**
     * Adds a changed entity to the set.
     * @param entity the changed entity.
     */
    protected void addChangedEntity(Entity entity) {
        changedEntities.add(entity);
    }

    /**
     * Removes an entity from the set.
     * @param entity the entity to remove.
     */
    protected void removeEntity(Entity entity) {
        if (entities.remove(entity)) {
            removedEntities.add(entity);
        }
    }

    /**
     * Retrieves the entities within the set.
     * @return the entities in the set.
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * @return the added entities since the last flush.
     */
    public List<Entity> getAddedEntities() {
        return addedEntities;
    }

    /**
     * Retrieves the entities that have changed since the last flush.
     * Recently added entities will not be in the changed entities list.
     * @return the entities that changed since the last flush.
     */
    public List<Entity> getChangedEntities() {
        return changedEntities;
    }

    /**
     * @return the removed entities since the last flush.
     */
    public List<Entity> getRemovedEntities() {
        return removedEntities;
    }

    /**
     * Checks if the set contains the entity.
     * @param entity the entity to check with.
     * @return true if the set contains the entity.
     */
    public boolean contains(Entity entity) {
        return entities.contains(entity);
    }

    /**
     * Flush changes for the set.
     */
    public void flushChanges() {
        addedEntities.clear();
        changedEntities.clear();
        removedEntities.clear();
    }

    /**
     * Clears the set.
     */
    protected void clear() {
        entities.clear();
        addedEntities.clear();
        changedEntities.clear();
        removedEntities.clear();
    }
}

package com.exploringlines.entitysystem;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/17/13
 * Time: 12:08 AM
 * An entity for the entity system.
 * The entity itself does not hold any components to prevent using the entity directly.
 */
public class Entity {
    /**
     * ID of the entity.
     */
    private long id;

    /**
     * Name of the entity which is useful in networked games where
     * the ID does not need to be in sync with the players.
     */
    private String name;

    /**
     * Initializes the entity.
     * @param id the id of the entity.
     */
    public Entity(long id) {
        this.id = id;
    }

    /**
     * Initializes the entity.
     * @param id the id of the entity.
     * @param name the name of the entity.
     */
    public Entity(long id, String name) {
        this(id);
        this.name = name;
    }

    /**
     * @return the id of the entity.
     */
    public long getId() {
        return id;
    }

    /**
     * @return the name of the entity.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.exploringlines.entitysystem;

import com.exploringlines.entitysystem.Entity;
import com.exploringlines.entitysystem.EntitySet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/17/13
 * Time: 3:00 PM
 */
public class EntitySetTest {
    /**
     * Entity set to test with.
     */
    private EntitySet entitySet;

    /**
     * An entity to test with.
     */
    private Entity entity;

    @Before
    public void setUp() throws Exception {
        entitySet = new EntitySet();
        entitySet.addEntity(new Entity(0));
        entity = new Entity(15);
        entitySet.addEntity(entity);
    }

    @Test
    public void testAddEntity() throws Exception {
        Entity entity = new Entity(1);
        entitySet.addEntity(entity);
        Assert.assertTrue(entitySet.contains(entity));
        entitySet.addEntity(entity);
        Assert.assertEquals(3, entitySet.getEntities().size());
    }

    @Test
    public void testRemoveEntity() throws Exception {
        Assert.assertEquals(2, entitySet.getEntities().size());
        entitySet.removeEntity(entity);
        Assert.assertEquals(1, entitySet.getEntities().size());
        entitySet.removeEntity(entity);
        Assert.assertEquals(1, entitySet.getEntities().size());
    }

    @Test
    public void testGetEntities() throws Exception {
        Assert.assertEquals(2, entitySet.getEntities().size());
    }

    @Test
    public void testContains() throws Exception {
        Assert.assertTrue(entitySet.contains(entity));
    }

    @Test
    public void testClear() throws Exception {
        Assert.assertEquals(2, entitySet.getEntities().size());
        entitySet.clear();
        Assert.assertEquals(0, entitySet.getEntities().size());
    }
}

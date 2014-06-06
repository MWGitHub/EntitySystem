package com.exploringlines.entitysystem;

import com.exploringlines.entitysystem.Component;
import com.exploringlines.entitysystem.Entity;
import com.exploringlines.entitysystem.EntitySet;
import com.exploringlines.entitysystem.EntitySystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/17/13
 * Time: 3:11 PM
 */
public class EntitySystemTest {
    private class TestComponent implements Component {
        public String test = "A String";

        @Override
        public Component copy() {
            return null;
        }
    }

    private class TestComponent2 implements Component {
        public int something = 50;

        @Override
        public Component copy() {
            return null;
        }
    }

    /**
     * Entity system to test with.
     */
    private EntitySystem entitySystem;

    @Before
    public void setUp() throws Exception {
        entitySystem = new EntitySystem();
        entitySystem.createEntity();
        entitySystem.createEntity("Test");
        entitySystem.setComponent(entitySystem.getEntity("Test"), new TestComponent());
        entitySystem.flushSetChanges();
    }

    @Test
    public void testCreateEntity() throws Exception {
        // Test ID only creation.
        Entity entity = entitySystem.createEntity();
        Assert.assertNotNull(entity);
        Assert.assertEquals(2, entity.getId());
        Assert.assertNull(entity.getName());

        // Test String and counter.
        entity = entitySystem.createEntity("Test");
        Assert.assertEquals(3, entity.getId());
        Assert.assertEquals("Test", entity.getName());
    }

    @Test
    public void testGetEntity() throws Exception {
        // Test ID retrieval.
        Entity entity = entitySystem.getEntity(0);
        Assert.assertNotNull(entity);
        Assert.assertNull(entitySystem.getEntity(2));

        // Test name retrieval.
        entity = entitySystem.getEntity("Test");
        Assert.assertNotNull(entity);
        Assert.assertNull(entitySystem.getEntity("None"));
    }

    @Test
    public void testRemoveEntity() throws Exception {
        // Test for the removal list.
        EntitySet set = entitySystem.getEntities(TestComponent.class);
        set.flushChanges();

        // Test the remove entity method.
        Entity testEntity = entitySystem.getEntity("Test");
        Assert.assertNotNull(entitySystem.getEntity("Test"));
        entitySystem.removeEntity(entitySystem.getEntity("Test"));
        Assert.assertNull(entitySystem.getEntity("Test"));

        // Test for set list removal.
        Assert.assertEquals(1, set.getRemovedEntities().size());
        Assert.assertEquals(0, set.getAddedEntities().size());
        Assert.assertEquals(0, set.getChangedEntities().size());
        Assert.assertNotNull(entitySystem.getComponent(set.getRemovedEntities().get(0), TestComponent.class));
        Assert.assertNotNull(entitySystem.getComponent(testEntity, TestComponent.class));
        entitySystem.flushSetChanges();
        Assert.assertNull(entitySystem.getComponent(testEntity, TestComponent.class));
    }

    @Test
    public void testGetComponent() throws Exception {
        TestComponent component = entitySystem.getComponent(entitySystem.getEntity("Test"), TestComponent.class);
        Assert.assertEquals("A String", component.test);
        Assert.assertNull(entitySystem.getComponent(entitySystem.getEntity("Test"), Component.class));
    }

    @Test
    public void testSetComponent() throws Exception {
        // Check the set lists.
        EntitySet set = entitySystem.getEntities(TestComponent2.class);
        set.flushChanges();

        // Test the add method.
        entitySystem.setComponent(entitySystem.getEntity("Test"), new TestComponent2());
        Assert.assertEquals(50, entitySystem.getComponent(entitySystem.getEntity("Test"), TestComponent2.class).something);
        Assert.assertEquals("A String", entitySystem.getComponent(entitySystem.getEntity("Test"), TestComponent.class).test);

        // Test the add list.
        Assert.assertEquals(0, set.getRemovedEntities().size());
        Assert.assertEquals(1, set.getAddedEntities().size());
        Assert.assertEquals(0, set.getChangedEntities().size());

        // Test the changed list.
        entitySystem.setComponent(entitySystem.getEntity("Test"), new TestComponent2());
        entitySystem.setComponent(entitySystem.getEntity("Test"), new TestComponent2());
        Assert.assertEquals(0, set.getRemovedEntities().size());
        Assert.assertEquals(1, set.getAddedEntities().size());
        Assert.assertEquals(2, set.getChangedEntities().size());
    }

    @Test
    public void testHasComponent() throws Exception {
        Assert.assertTrue(entitySystem.hasComponent(entitySystem.getEntity("Test"), TestComponent.class));
        Assert.assertFalse(entitySystem.hasComponent(entitySystem.getEntity("Test"), TestComponent2.class));
    }

    @Test
    public void testRemoveComponent() throws Exception {
        // Test for removal list too.
        EntitySet set = entitySystem.getEntities(TestComponent.class);
        set.flushChanges();

        // Test the remove component method.
        entitySystem.removeComponent(entitySystem.getEntity("Test"), TestComponent.class);
        Assert.assertNull(entitySystem.getComponent(entitySystem.getEntity("Test"), TestComponent.class));

        // Test for set list removal.
        Assert.assertEquals(1, set.getRemovedEntities().size());
        Assert.assertEquals(0, set.getAddedEntities().size());
        Assert.assertEquals(0, set.getChangedEntities().size());
    }

    @Test
    public void testGetEntities() throws Exception {
        EntitySet set = entitySystem.getEntities(TestComponent.class);
        Assert.assertTrue(set.contains(entitySystem.getEntity("Test")));
        entitySystem.removeComponent(entitySystem.getEntity("Test"), TestComponent.class);
        Assert.assertFalse(set.contains(entitySystem.getEntity("Test")));
    }

    @Test
    public void testGetComponentsOfType() throws Exception {
        Collection<TestComponent> components = entitySystem.getComponentsOfType(TestComponent.class);
        Assert.assertEquals(1, components.size());
        Collection<TestComponent2> components2 = entitySystem.getComponentsOfType(TestComponent2.class);
        Assert.assertEquals(0, components2.size());
    }
}

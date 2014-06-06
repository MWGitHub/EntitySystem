package com.exploringlines.entitysystem;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/26/13
 * Time: 11:55 AM
 * Optional interface to use for subsystems to allow for an easy way to
 * manage all subsystems.
 */
public interface Subsystem {
    /**
     * Updates the system.
     * @param tpf the time per frame.
     */
    void update(float tpf);

    /**
     * Should be updated at the end of the update loop.
     * Used mainly for resetting states.
     * Systems should never depend on another system in the cleanup step.
     */
    void cleanupSubsystem();

    /**
     * Destroys the system.
     */
    void destroy();
}

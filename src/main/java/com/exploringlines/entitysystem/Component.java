package com.exploringlines.entitysystem;

/**
 * Created with IntelliJ IDEA.
 * User: MW
 * Date: 4/17/13
 * Time: 12:24 PM
 * Components should hold only data and no methods.
 * Constructors are allowed as long as there is a default constructor that takes no parameters.
 * For best practice do not put any logic other than setting variables into constructors.
 */
public interface Component {
    /**
     * Creates a copy of the component.
     * @return a copy of the component.
     */
    Component copy();
}

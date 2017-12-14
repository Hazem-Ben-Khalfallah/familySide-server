package com.blacknebula.familySide.connection;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hazem
 */
public class CustomUserConnectionRepositoryTest {
    /**
     * @verifies return true if there is a connection between 2 users
     * @see CustomUserConnectionRepository#checkConnectionExistence(String, String)
     */
    @Test
    public void checkConnectionExistence_shouldReturnTrueIfThereIsAConnectionBetween2Users() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return false if there is no connection between 2 users
     * @see CustomUserConnectionRepository#checkConnectionExistence(String, String)
     */
    @Test
    public void checkConnectionExistence_shouldReturnFalseIfThereIsNoConnectionBetween2Users() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return all connections if status is null
     * @see CustomUserConnectionRepository#findByUsernameAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUsernameAndConnectionStatus_shouldReturnAllConnectionsIfStatusIsNull() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies filter connections if status is not null
     * @see CustomUserConnectionRepository#findByUsernameAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUsernameAndConnectionStatus_shouldFilterConnectionsIfStatusIsNotNull() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return empty flux if status is null and the user has no connections
     * @see CustomUserConnectionRepository#findByUsernameAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUsernameAndConnectionStatus_shouldReturnEmptyFluxIfStatusIsNullAndTheUserHasNoConnections() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return empty flux if the user has no connections with the specified status
     * @see CustomUserConnectionRepository#findByUsernameAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUsernameAndConnectionStatus_shouldReturnEmptyFluxIfTheUserHasNoConnectionsWithTheSpecifiedStatus() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }
}

package com.blacknebula.familySide.connection;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author hazem
 */
public class UserConnectionServiceTest {
    /**
     * @verifies throw an exception if username is empty
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfUsernameIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if targetUsername is empty
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfTargetUsernameIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if username does not exists
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfUsernameDoesNotExists() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if targetUsername does not exists
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfTargetUsernameDoesNotExists() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if there is already an ongoing request for the same targetUsername
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfThereIsAlreadyAnOngoingRequestForTheSameTargetUsername() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies create a connection between user an the target with a pending status
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldCreateAConnectionBetweenUserAnTheTargetWithAPendingStatus() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies should throw an exception if username is empty
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldShouldThrowAnExceptionIfUsernameIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies should throw an exception if username does not exists
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldShouldThrowAnExceptionIfUsernameDoesNotExists() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies list all connection requests related to a user
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldListAllConnectionRequestsRelatedToAUser() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return filtered connection requests list related to a user
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturnFilteredConnectionRequestsListRelatedToAUser() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return empty flux if the user has no connections
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturnEmptyFluxIfTheUserHasNoConnections() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if username is empty
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfUsernameIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if connectionId is empty
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfConnectionIdIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if username does not exists
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfUsernameDoesNotExists() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if connectionId does not exists
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfConnectionIdDoesNotExists() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if connectionId does not target the accepting user
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfConnectionIdDoesNotTargetTheAcceptingUser() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies change connection request status to accepted
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldChangeConnectionRequestStatusToAccepted() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

}

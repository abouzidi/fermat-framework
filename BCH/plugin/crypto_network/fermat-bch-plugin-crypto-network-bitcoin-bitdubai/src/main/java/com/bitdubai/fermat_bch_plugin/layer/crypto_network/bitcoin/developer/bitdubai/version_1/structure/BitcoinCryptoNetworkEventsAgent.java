package com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.AbstractAgent;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.EventManager;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.crypto_transactions.CryptoStatus;
import com.bitdubai.fermat_bch_api.layer.crypto_network.enums.TransactionTypes;
import com.bitdubai.fermat_bch_api.layer.definition.event_manager.enums.EventType;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.database.BitcoinCryptoNetworkDatabaseDao;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.CantExecuteDatabaseOperationException;

import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * The Class <code>com.bitdubai.fermat_bch_plugin.layer.cryptonetwork.bitcoin.developer.bitdubai.version_1.structure.BitcoinCryptoNetworkEventsAgent</code>
 * Monitors the database for pending transactions to be notified, and raises the Events if pending transactions are foind.
 * <p/>
 *
 * Created by Rodrigo Acosta - (acosta_rodrigo@hotmail.com) on 13/10/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class BitcoinCryptoNetworkEventsAgent extends AbstractAgent{
    /**
     * class variables
     */
    private final BitcoinCryptoNetworkDatabaseDao dao;

    /**
     * platform variables
     */
    EventManager eventManager;

    /**
     * Constructor
     * @param eventManager
     * @param bitcoinCryptoNetworkDatabaseDao
     */
    public BitcoinCryptoNetworkEventsAgent(EventManager eventManager,
                                           BitcoinCryptoNetworkDatabaseDao bitcoinCryptoNetworkDatabaseDao) {
        super(10, TimeUnit.SECONDS); //this agent will run every ten seconds.

        this.eventManager = eventManager;
        this.dao = bitcoinCryptoNetworkDatabaseDao;
    }

        /**
         * performs the main task
         */
        private void doTheMainTask() {
            /**
             * I will get the pending transactions to be notified on each table
             */
            int pendingIncoming = getPendingNotifiedIncomingTransactions();
            int pendingOutgoing = getPendingNotifiedOutgoingTransactions();

            /**
             * update the statistics of the agent
             */
            updateEventAgentStats(pendingIncoming, pendingOutgoing);

            /**
             * if there are pending transactions then fix any missing information we may have
             * and raise the notification events for each CryptoStatus
             */
            if (pendingIncoming > 0){
                fixMissingInformationFromIncomingTransactions();
                raiseIncomingTransactionEvent();
            }


            if (pendingOutgoing > 0){
                fixMissingInformationFromOutgoingTransactions();
                raiseOutgoingTransactionEvent();
            }
        }

        /**
         * Since the Transaction may not be complete in all cases, we need to make sure the information is
         * accurate by getting the information from the wallet and update the values.
         */
        private void fixMissingInformationFromIncomingTransactions() {
            //todo complete
            //get tx info from vault
            //update table information
        }

        /**
         * Since the Transaction may not be complete in all cases, we need to make sure the information is
         * accurate by getting the information from the wallet and update the values.
         */
        private void fixMissingInformationFromOutgoingTransactions() {
            //todo complete
            //get tx info from vault
            //update table information
        }

        /**
         * will raise each event for the crypto status found in pending status
         */
        private void raiseOutgoingTransactionEvent() {
            try {
                Set<CryptoStatus> cryptoStatuses = dao.getPendingCryptoStatus(TransactionTypes.OUTGOING);
                for (CryptoStatus cryptoStatus : cryptoStatuses){
                    raiseOutgoingEvent(cryptoStatus);
                }
            } catch (CantExecuteDatabaseOperationException e) {
                e.printStackTrace();
            }

        }

        /**
         * Raises the appropiate outgoing events depending on the crypto status
         * @param cryptoStatus
         */
        private void raiseOutgoingEvent(CryptoStatus cryptoStatus) {
            FermatEvent event = null;
            switch (cryptoStatus){
                case ON_CRYPTO_NETWORK:
                    //event = eventManager.getNewEvent(EventType.OUTGOING_CRYPTO_ON_CRYPTO_NETWORK);
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK);
                    break;
                case ON_BLOCKCHAIN:
                    //event = eventManager.getNewEvent(EventType.OUTGOING_CRYPTO_ON_BLOCKCHAIN);
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_ON_BLOCKCHAIN);
                    break;
                case REVERSED_ON_CRYPTO_NETWORK:
                    //event = eventManager.getNewEvent(EventType.OUTGOING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK);
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK);
                    break;
                case REVERSED_ON_BLOCKCHAIN:
                    //event = eventManager.getNewEvent(EventType.OUTGOING_CRYPTO_REVERSED_ON_BLOCKCHAIN);
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN);
                    break;
                case IRREVERSIBLE:
                    //event = eventManager.getNewEvent(EventType.OUTGOING_CRYPTO_IRREVERSIBLE);
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_IRREVERSIBLE);
                    break;
            }
            /**
             * I raise the event
             */
            event.setSource(EventSource.CRYPTO_NETWORK_BITCOIN_PLUGIN);
            eventManager.raiseEvent(event);
        }

        /**
         * will get all the CryptoStatus from the pending transactions
         */
        private void raiseIncomingTransactionEvent() {
            try {
                Set<CryptoStatus> cryptoStatuses = dao.getPendingCryptoStatus(TransactionTypes.INCOMING);
                for (CryptoStatus cryptoStatus : cryptoStatuses){
                    raiseIncomingEvent(cryptoStatus);
                }
            } catch (CantExecuteDatabaseOperationException e) {
                e.printStackTrace();
            }
        }

        /**
         * executes the raise of the event for incoming transactions
         * @param cryptoStatus
         */
        private void raiseIncomingEvent(CryptoStatus cryptoStatus) {
            FermatEvent event = null;
            switch (cryptoStatus){
                case ON_CRYPTO_NETWORK:
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_ON_CRYPTO_NETWORK);
                    break;
                case ON_BLOCKCHAIN:
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_ON_BLOCKCHAIN);
                    break;
                case REVERSED_ON_CRYPTO_NETWORK:
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_REVERSED_ON_CRYPTO_NETWORK);
                    break;
                case REVERSED_ON_BLOCKCHAIN:
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_REVERSED_ON_BLOCKCHAIN);
                    break;
                case IRREVERSIBLE:
                    event = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_IRREVERSIBLE);
                    break;
            }
            /**
             * I raise the event
             */
            event.setSource(EventSource.CRYPTO_NETWORK_BITCOIN_PLUGIN);
            eventManager.raiseEvent(event);
        }

        /**
         * updates the statistics of the agent execution
         * @param pendingIncoming
         * @param pendingOutgoing
         */
        private void updateEventAgentStats(int pendingIncoming, int pendingOutgoing) {
            try {
                dao.updateEventAgentStats(pendingIncoming, pendingOutgoing);
            } catch (Exception e) {
                //if stats are not updated, we can go on.
                e.printStackTrace();
            }
        }

        /**
         * searchs the database for PendingNotified protocolStatus transactions
         * @return
         */
        private int getPendingNotifiedIncomingTransactions(){
            try {
                return dao.getPendingNotifiedTransactions(TransactionTypes.INCOMING);
            } catch (CantExecuteDatabaseOperationException e) {
                return 0;
            }
        }

        /**
         * searchs the database for PendingNotified protocolStatus transactions
         * @return
         */
        private int getPendingNotifiedOutgoingTransactions(){
            try {
                return dao.getPendingNotifiedTransactions(TransactionTypes.OUTGOING);
            } catch (CantExecuteDatabaseOperationException e) {
                return 0;
            }
        }



    @Override
    protected void agentJob() {
        doTheMainTask();
    }

    @Override
    protected void onErrorOccur(Exception e) {

    }

}

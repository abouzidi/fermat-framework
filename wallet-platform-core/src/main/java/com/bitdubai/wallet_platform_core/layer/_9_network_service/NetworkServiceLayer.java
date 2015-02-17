package com.bitdubai.wallet_platform_core.layer._9_network_service;

import com.bitdubai.wallet_platform_api.Plugin;
import com.bitdubai.wallet_platform_api.layer.CantStartLayerException;
import com.bitdubai.wallet_platform_api.layer.PlatformLayer;
import com.bitdubai.wallet_platform_api.layer._9_network_service.CantStartSubsystemException;
import com.bitdubai.wallet_platform_api.layer._9_network_service.NetworkSubsystem;
import com.bitdubai.wallet_platform_core.layer._9_network_service.bank_notes.BankNotesSubsystem;
import com.bitdubai.wallet_platform_core.layer._9_network_service.user.UserSubsystem;
import com.bitdubai.wallet_platform_core.layer._9_network_service.wallet_resources.WalletResourcesSubsystem;
import com.bitdubai.wallet_platform_core.layer._9_network_service.wallet_store.WalletStoreSubsystem;

/**
 * Created by ciencias on 03.01.15.
 */
public class NetworkServiceLayer implements PlatformLayer {

    private Plugin mUserPlugin;

    private Plugin mBankNotesPlugin;
    
    private Plugin mWalletResources;

    private Plugin mWalletStore;


    public Plugin getUserPlugin() {
        return mUserPlugin;
    }

    public Plugin getBankNotesPlugin() {
        return mBankNotesPlugin;
    }

    public Plugin getWalletResources() {
        return mWalletResources;
    }

    public Plugin getWalletStore() {
        return mWalletStore;
    }

    
    @Override
    public void start() throws CantStartLayerException {

        /**
         * The most essential service is the UserPlugin. I start it now.
         */

        NetworkSubsystem userSubsystem = new UserSubsystem();

        try {
            userSubsystem.start();
            mUserPlugin = ((UserSubsystem) userSubsystem).getPlugin();

        } catch (CantStartSubsystemException e) {
            System.err.println("CantStartCryptoNetworkException: " + e.getMessage());

            /**
             * Since this is the only implementation, if this does not start, then the layer can't start either.
             */

            throw new CantStartLayerException();
    
        }


        /**
         * Let's try to start the Bank Notes subsystem.
         */

        NetworkSubsystem bankNotesSubsytem = new BankNotesSubsystem();

        try {
            bankNotesSubsytem.start();
            mBankNotesPlugin = (bankNotesSubsytem).getPlugin();

        } catch (CantStartSubsystemException e) {
            System.err.println("CantStartCryptoNetworkException: " + e.getMessage());

            /**
             * Since this is the only implementation, if this does not start, then the layer can't start either.
             */

            throw new CantStartLayerException();

        }


        /**
         * Let's try to start the Wallet Resources subsystem.
         */

        NetworkSubsystem walletResources = new WalletResourcesSubsystem();

        try {
            walletResources.start();
            mWalletResources = (walletResources).getPlugin();

        } catch (CantStartSubsystemException e) {
            System.err.println("CantStartCryptoNetworkException: " + e.getMessage());

            /**
             * Since this is the only implementation, if this does not start, then the layer can't start either.
             */

            throw new CantStartLayerException();

        }

        /**
         * Let's try to start the Wallet Store subsystem.
         */

        NetworkSubsystem walletStore = new WalletStoreSubsystem();

        try {
            walletStore.start();
            mWalletStore = (walletStore).getPlugin();

        } catch (CantStartSubsystemException e) {
            System.err.println("CantStartCryptoNetworkException: " + e.getMessage());

            /**
             * Since this is the only implementation, if this does not start, then the layer can't start either.
             */

            throw new CantStartLayerException();

        }
    }
}

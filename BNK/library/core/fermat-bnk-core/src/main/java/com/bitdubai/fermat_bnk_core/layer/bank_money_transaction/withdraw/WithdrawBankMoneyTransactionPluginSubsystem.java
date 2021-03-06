package com.bitdubai.fermat_bnk_core.layer.bank_money_transaction.withdraw;

import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_bnk_plugin.layer.bank_money_transaction.withdraw.developer.bitdubai.DeveloperBitDubai;

/**
 * Created by memo on 25/11/15.
 */
public class WithdrawBankMoneyTransactionPluginSubsystem extends AbstractPluginSubsystem {
    public WithdrawBankMoneyTransactionPluginSubsystem() {
        super(new PluginReference(Plugins.BITDUBAI_BNK_WITHDRAW_MONEY_TRANSACTION));
    }

    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());

        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }

}

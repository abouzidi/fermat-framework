package com.bitdubai.fermat_dap_android_wallet_asset_user_bitdubai.v2.models;

import com.bitdubai.fermat_android_api.ui.expandableRecicler.ParentListItem;

import java.util.List;

/**
 * Created by Frank Contreras (contrerasfrank@gmail.com) on 2/26/16.
 */
public class GrouperItem<CHILD_TYPE,ITEM> implements ParentListItem {

    private List<CHILD_TYPE> assets;
    private int assetsCount;
    private boolean initiallyExpanded;

    private ITEM issuer;

    public GrouperItem( List<CHILD_TYPE> assets, boolean initiallyExpanded, ITEM issuer) {
        this.initiallyExpanded = initiallyExpanded;
        this.assets = assets;
        this.issuer = issuer;
    }

    public GrouperItem(String text, boolean initiallyExpanded) {
        this.initiallyExpanded = initiallyExpanded;
    }

    public int getAssetsCount() {
        if (assets != null)
            // Le resto uno porque el ultimo es el total
            assetsCount = this.assets.size();
        return assetsCount;
    }

    /**
     * Getter method for the list of children associated with this parent list item
     *
     * @return list of all children associated with this specific parent list item
     */
    @Override
    public List<CHILD_TYPE> getChildItemList() {
        return assets;
    }

    /**
     * Setter method for the list of children associated with this parent list item
     *
     * @param assets the list of all children associated with this parent list item
     */
    public void setAssets(List<CHILD_TYPE> assets) {
        this.assets = assets;
        if (assets != null)
            assetsCount = this.assets.size();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return initiallyExpanded;
    }

    public void setInitiallyExpanded(boolean initiallyExpanded) {
        this.initiallyExpanded = initiallyExpanded;
    }

    public ITEM getIssuer() {
        return issuer;
    }
}

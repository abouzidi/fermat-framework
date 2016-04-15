package com.bitdubai.sub_app.chat_community.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunityInformation;
import com.bitdubai.sub_app.chat_community.R;
import com.bitdubai.sub_app.chat_community.holders.CommunityWorldHolder;

import java.util.List;

/**
 * CommunityListAdapter
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 13/04/16.
 * @version 1.0
 */

@SuppressWarnings("unused")
public class CommunityListAdapter extends FermatAdapter<ChatActorCommunityInformation, CommunityWorldHolder> {

    public CommunityListAdapter(Context context) {
        super(context);
    }

    public CommunityListAdapter(Context context, List<ChatActorCommunityInformation> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected CommunityWorldHolder createHolder(View itemView, int type) {
        return new CommunityWorldHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.cht_comm_world_item;
    }

    @Override
    protected void bindHolder(CommunityWorldHolder holder, ChatActorCommunityInformation data, int position) {
        holder.connectionState.setVisibility(View.GONE);
        ConnectionState connectionState = data.getActorConnectionState();
        switch (connectionState) {
            case CONNECTED:
                if (holder.connectionState.getVisibility() == View.GONE)
                    holder.connectionState.setVisibility(View.VISIBLE);
                break;
            case BLOCKED_LOCALLY:
                break;
            case BLOCKED_REMOTELY:
                break;
            case CANCELLED_LOCALLY:
                break;
            case CANCELLED_REMOTELY:
                if (holder.connectionState.getVisibility() == View.GONE){
                    holder.connectionState.setImageResource(R.drawable.icon_contact_no_conect);
                    holder.connectionState.setVisibility(View.VISIBLE);
                }
                break;
            case NO_CONNECTED:
                break;
            case DENIED_LOCALLY:
                break;
            case DENIED_REMOTELY:
                if (holder.connectionState.getVisibility() == View.GONE){
                    holder.connectionState.setImageResource(R.drawable.icon_contact_no_conect);
                    holder.connectionState.setVisibility(View.VISIBLE);
                }
                break;
            case DISCONNECTED_LOCALLY:
                break;
            case DISCONNECTED_REMOTELY:
                break;
            case ERROR:
                break;
            case INTRA_USER_NOT_FOUND:
                break;
            case PENDING_LOCALLY_ACCEPTANCE:
                break;
            case PENDING_REMOTELY_ACCEPTANCE:
                if (holder.connectionState.getVisibility() == View.GONE){
                    holder.connectionState.setImageResource(R.drawable.icon_contact_standby);
                    holder.connectionState.setVisibility(View.VISIBLE);
                }
                break;
            default:
                if (holder.connectionState.getVisibility() == View.VISIBLE)
                    holder.connectionState.setVisibility(View.GONE);
                break;
        }
        holder.row_connection_state.setText(data.getActorConnectionState().toString());//data.getState()
        if(data.getActorConnectionState().toString().equals("Offline"))
            holder.row_connection_state.setTextColor(Color.RED);
        else
            holder.row_connection_state.setTextColor(Color.WHITE);
        holder.name.setText(data.getActorAlias());
        byte[] profileImage = data.getActorImage();
        if (profileImage != null && profileImage.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
            bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, true);
            holder.thumbnail.setImageBitmap(bitmap);
        }

    }

    public int getSize() {
        if (dataSet != null)
            return dataSet.size();
        return 0;
    }
}
package com.bitdubai.sub_app.intra_user_community.holders;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.SquareImageView;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.fermat_ccp_api.layer.module.intra_user.interfaces.IntraUserInformation;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.ProfileStatus;
import com.bitdubai.sub_app.intra_user_community.R;

/**
 * Created by natalia on 13/07/16.
 */
public class  AvailableActorsViewHolder extends FermatViewHolder {

    public SquareImageView thumbnail;
    public FermatTextView name;
    public ImageView connectionState;
    public FermatTextView row_connection_state;
    public ProgressBar progressBar;
    private TextView button_add;
    private Resources res;
    private TextView response;
    private FermatTextView location;

    /**
     * Constructor
     *
     * @param itemView cast elements in layout
     * @param type     the view older type ID
     */
    public AvailableActorsViewHolder(View itemView, int type) {
        super(itemView, type);
        res = itemView.getResources();
        response = (TextView) itemView.findViewById(R.id.response);
        button_add = (TextView) itemView.findViewById(R.id.button_add);
        //connectionState = (ImageView) itemView.findViewById(R.id.connection_state);
        row_connection_state = (FermatTextView) itemView.findViewById(R.id.connection_state_user);
        thumbnail = (SquareImageView) itemView.findViewById(R.id.profile_image);
        name = (FermatTextView) itemView.findViewById(R.id.community_name);
        location =(FermatTextView) itemView.findViewById(R.id.location);

        // progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);


    }

    public void bind(IntraUserInformation data) {


        String city = data.getCity();
        String country = data.getCountry();

        if (city!=null && country!=null)
            if (!city.equals("----") && !country.equals("----"))
                if (country!=null &&(city==null || city=="----" || city==""))
                    location.setText(country);
                else if (city!=null &&(country==null || country=="----" || country==""))
                    location.setText(city);
                else location.setText(country+", "+city);
            else location.setText("No Location");
        else location.setText("No Location");

        row_connection_state.setText((data.getState().equals(ProfileStatus.ONLINE)) ?
                res.getString(R.string.connectionState_online) : res.getString(R.string.connectionState_offline) );
        if(data.getState().equals(ProfileStatus.OFFLINE))
        {
            //button_add.setBackgroundColor(Color.RED);
            button_add.setVisibility(View.GONE);
            response.setText(res.getString(R.string.connectionState_offline) );
            response.setTextColor(Color.RED);
            response.setVisibility(View.VISIBLE);
        }
        // else{
        //   button_add.setBackgroundColor(Color.parseColor("#21386D"));
        //  button_add.setVisibility(View.VISIBLE);
        //  }

        if (data.getConnectionState() != null)
        {
            switch (data.getConnectionState()) {
                case CONNECTED:
                    response.setVisibility(View.VISIBLE);
                    response.setText(res.getString(R.string.connectionState_connected));
                    response.setTextColor(Color.parseColor("#21386D"));
                    button_add.setVisibility(View.GONE);
                    break;
                case BLOCKED_LOCALLY:
                    break;
                case BLOCKED_REMOTELY:
                    break;
                case CANCELLED_LOCALLY:
                    break;
                case CANCELLED_REMOTELY:
                    //connectionState.setImageResource(R.drawable.icon_contact_no_conect);
                    response.setText(res.getString(R.string.connectionState_request_canceled));
                    response.setVisibility(View.VISIBLE);
                    response.setTextColor(Color.parseColor("#21386D"));
                    button_add.setVisibility(View.GONE);

                    break;
                case NO_CONNECTED:
                    if(data.getState().equals(ProfileStatus.OFFLINE))
                    {
                        //button_add.setBackgroundColor(Color.RED);
                        button_add.setVisibility(View.GONE);
                        response.setText(res.getString(R.string.connectionState_offline));
                        response.setTextColor(Color.RED);
                        response.setVisibility(View.VISIBLE);
                    }else
                    {
                        response.setText("");
                        response.setVisibility(View.GONE);
                        button_add.setVisibility(View.VISIBLE);
                    }


                    break;
                case DENIED_LOCALLY:
                    if(data.getState().equals(ProfileStatus.OFFLINE))
                    {
                        //button_add.setBackgroundColor(Color.RED);
                        button_add.setVisibility(View.GONE);
                        response.setText(res.getString(R.string.connectionState_offline));
                        response.setTextColor(Color.RED);
                        response.setVisibility(View.VISIBLE);
                    }else
                    {
                        response.setText("");
                        response.setVisibility(View.GONE);
                        button_add.setVisibility(View.VISIBLE);
                    }

                    break;
                case DENIED_REMOTELY:

                    response.setText(res.getString(R.string.connectionState_denied_remotely));
                    response.setTextColor(Color.parseColor("#21386D"));
                    response.setVisibility(View.VISIBLE);
                    button_add.setVisibility(View.GONE);

                    break;
                case DISCONNECTED_LOCALLY:
                    break;
                case DISCONNECTED_REMOTELY:
                    if(data.getState().equals(ProfileStatus.OFFLINE))
                    {
                        //button_add.setBackgroundColor(Color.RED);
                        button_add.setVisibility(View.GONE);
                        response.setText(res.getString(R.string.connectionState_offline));
                        response.setTextColor(Color.RED);
                        response.setVisibility(View.VISIBLE);
                    }else
                    {
                        response.setText("");
                        response.setVisibility(View.GONE);
                        button_add.setVisibility(View.VISIBLE);
                    }

                    break;
                case ERROR:
                    break;
                case INTRA_USER_NOT_FOUND:
                    break;
                case PENDING_LOCALLY_ACCEPTANCE:
                    response.setText(res.getString(R.string.connectionState_request_received));
                    response.setTextColor(Color.parseColor("#21386D"));
                    //connectionState.setImageResource(R.drawable.icon_contact_standby);
                    response.setVisibility(View.VISIBLE);
                    button_add.setVisibility(View.GONE);
                    break;
                case PENDING_REMOTELY_ACCEPTANCE:

                    response.setText(res.getString(R.string.connectionState_request_sent));
                    response.setTextColor(Color.parseColor("#21386D"));
                    //connectionState.setImageResource(R.drawable.icon_contact_standby);
                    response.setVisibility(View.VISIBLE);
                    button_add.setVisibility(View.GONE);

                    break;
                default:
                    if (response.getVisibility() == View.VISIBLE)
                        response.setVisibility(View.GONE);
                    button_add.setVisibility(View.VISIBLE);
                    break;
            }


            name.setText(data.getName());
            byte[] profileImage = data.getProfileImage();
            if (profileImage != null && profileImage.length > 0) {
                //Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
                //bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, true);
                thumbnail.setImageDrawable(  getImgDrawable(profileImage));
            }else{
                thumbnail.setVisibility(View.GONE);

            }
        }// else {
           // connectionState.setVisibility(View.INVISIBLE);

       // }



    }

    private Drawable getImgDrawable(byte[] customerImg) {
        if (customerImg != null && customerImg.length > 0)
            return ImagesUtils.getRoundedBitmap(res, customerImg);

        return ImagesUtils.getRoundedBitmap(res, R.drawable.ic_profile_male);
    }
}

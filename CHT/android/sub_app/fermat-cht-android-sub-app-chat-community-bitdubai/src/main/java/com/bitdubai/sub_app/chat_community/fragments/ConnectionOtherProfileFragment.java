package com.bitdubai.sub_app.chat_community.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.actor_connection.common.enums.ConnectionState;
import com.bitdubai.fermat_api.layer.modules.exceptions.ActorIdentityNotSelectedException;
import com.bitdubai.fermat_api.layer.modules.exceptions.CantGetSelectedActorIdentityException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetChatUserIdentityException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions.CantValidateActorConnectionStateException;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunityInformation;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunitySelectableIdentity;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.interfaces.ChatActorCommunitySubAppModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.sub_app.chat_community.R;
import com.bitdubai.sub_app.chat_community.common.popups.AcceptDialog;
import com.bitdubai.sub_app.chat_community.common.popups.ConnectDialog;
import com.bitdubai.sub_app.chat_community.common.popups.DisconnectDialog;
import com.bitdubai.sub_app.chat_community.session.ChatUserSubAppSession;
import com.bitdubai.sub_app.chat_community.util.CommonLogger;

/**
 * ConnectionOtherProfileFragment
 *
 * @author Jose Cardozo josejcb (josejcb89@gmail.com) on 13/04/16.
 * @version 1.0
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ConnectionOtherProfileFragment extends AbstractFermatFragment
        implements View.OnClickListener {

    public static final String CHAT_USER_SELECTED = "chat_user";
    private String TAG = "ConnectionOtherProfileFragment";
    private Resources res;
    private View rootView;
    private ChatUserSubAppSession chatUserSubAppSession;
    private ImageView userProfileAvatar;
    private FermatTextView userName;
    private FermatTextView userEmail;
    private ChatActorCommunitySubAppModuleManager moduleManager;
    private ErrorManager errorManager;
    private ChatActorCommunityInformation chatUserInformation;
    private Button connect;
    private ChatActorCommunitySelectableIdentity identity;
    private Button disconnect;
    private int MAX = 1;
    private int OFFSET = 0;
    private FermatTextView userStatus;
    private Button connectionRequestSend;
    private Button connectionRequestRejected;
    private Button accept;
    //private IntraWalletUserActorManager intraWalletUserActorManager;
    private ConnectionState connectionState;
    private android.support.v7.widget.Toolbar toolbar;

    /**
     * Create a new instance of this fragment
     *
     * @return InstalledFragment instance object
     */
    public static ConnectionOtherProfileFragment newInstance() {
        return new ConnectionOtherProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // setting up  module
        chatUserSubAppSession = ((ChatUserSubAppSession) appSession);
        chatUserInformation = (ChatActorCommunityInformation) appSession.getData(CHAT_USER_SELECTED);
        moduleManager = chatUserSubAppSession.getModuleManager();
        errorManager = appSession.getErrorManager();
        chatUserInformation = (ChatActorCommunityInformation) appSession.getData(ConnectionsWorldFragment.CHAT_USER_SELECTED);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cht_comm_other_profile_fragment, container, false);
        toolbar = getToolbar();
        if (toolbar != null)
            toolbar.setTitle(chatUserInformation.getActorAlias());
        userProfileAvatar = (ImageView) rootView.findViewById(R.id.img_user_avatar);
        userStatus = (FermatTextView) rootView.findViewById(R.id.userPhrase);
        userName = (FermatTextView) rootView.findViewById(R.id.username);
        userEmail = (FermatTextView) rootView.findViewById(R.id.email);
        connectionRequestSend = (Button) rootView.findViewById(R.id.btn_connection_request_send);
        connectionRequestRejected = (Button) rootView.findViewById(R.id.btn_connection_request_reject);
        connect = (Button) rootView.findViewById(R.id.btn_conect);
        accept = (Button) rootView.findViewById(R.id.btn_connection_accept);
        disconnect = (Button) rootView.findViewById(R.id.btn_disconect);
        connectionRequestSend.setVisibility(View.GONE);
        connectionRequestRejected.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);
        disconnect.setVisibility(View.GONE);
        connectionRequestRejected.setOnClickListener(this);
        connectionRequestSend.setOnClickListener(this);
        connect.setOnClickListener(this);
        disconnect.setOnClickListener(this);
        accept.setOnClickListener(this);

        switch (chatUserInformation.getActorConnectionState()) {
                case BLOCKED_LOCALLY:
                case BLOCKED_REMOTELY:
                case CANCELLED_LOCALLY:
                case CANCELLED_REMOTELY:
                    connectionRejected();
                    break;
                case CONNECTED:
                    disconnectRequest();
                    break;
                case NO_CONNECTED:
                case DISCONNECTED_LOCALLY:
                case DISCONNECTED_REMOTELY:
                case ERROR:
                case DENIED_LOCALLY:
                case DENIED_REMOTELY:
                    connectRequest();
                    break;
                case PENDING_LOCALLY_ACCEPTANCE:
                    conectionAccept();
                    break;
                case PENDING_REMOTELY_ACCEPTANCE:
                    connectionSend();
                    break;
            }

        try {
            userName.setText(chatUserInformation.getActorAlias());
            userStatus.setText(chatUserInformation.getActorConnectionState().toString());
            userStatus.setTextColor(Color.parseColor("#292929"));
            if (chatUserInformation.getActorImage() != null) {
                Bitmap bitmap;
                if (chatUserInformation.getActorImage().length > 0) {
                    bitmap = BitmapFactory.decodeByteArray(chatUserInformation.getActorImage(), 0,
                            chatUserInformation.getActorImage().length);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cht_comm_bg_circular_other_profile);//profile_image);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, true);
                userProfileAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), bitmap));
            } else {
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cht_comm_bg_circular_other_profile);//profile_image);
                bitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, true);
                userProfileAvatar.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), bitmap));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_conect) {
            CommonLogger.info(TAG, "User connection state " +
                    chatUserInformation.getActorConnectionState());
            ConnectDialog connectDialog;
            try {
                connectDialog =
                        new ConnectDialog(getActivity(), (ChatUserSubAppSession) appSession, null,
                                chatUserInformation, moduleManager.getSelectedActorIdentity());
                connectDialog.setTitle("Connection Request");
                connectDialog.setDescription("Do you want to send ");
                connectDialog.setUsername(chatUserInformation.getActorAlias());
                connectDialog.setSecondDescription("a connection request");
                connectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateButton();
                    }
                });
                connectDialog.show();
            } catch ( CantGetSelectedActorIdentityException e) {
                e.printStackTrace();
            } catch ( ActorIdentityNotSelectedException e) {
                e.printStackTrace();
            }
        }
        if (i == R.id.btn_disconect) {
            CommonLogger.info(TAG, "User connection state " +
                    chatUserInformation.getActorConnectionState());
            final DisconnectDialog disconnectDialog;
            try {
                disconnectDialog =
                        new DisconnectDialog(getActivity(), (ChatUserSubAppSession) appSession, null,
                                chatUserInformation, moduleManager.getSelectedActorIdentity());
                disconnectDialog.setTitle("Disconnect");
                disconnectDialog.setDescription("Want to disconnect from");
                disconnectDialog.setUsername(chatUserInformation.getActorAlias());
                disconnectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateButton();
                    }
                });
                disconnectDialog.show();
            } catch ( CantGetSelectedActorIdentityException e) {
                e.printStackTrace();
            } catch ( ActorIdentityNotSelectedException e) {
                e.printStackTrace();
            }
        }
        if (i == R.id.btn_connection_accept){
            try {
                AcceptDialog notificationAcceptDialog =
                        new AcceptDialog(getActivity(),(ChatUserSubAppSession) appSession, null,
                                chatUserInformation, moduleManager.getSelectedActorIdentity());
                notificationAcceptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateButton();
                    }
                });
                notificationAcceptDialog.show();

            } catch ( CantGetSelectedActorIdentityException e) {
                e.printStackTrace();
            } catch ( ActorIdentityNotSelectedException e) {
                e.printStackTrace();
            }
        }
        if (i == R.id.btn_connection_request_send) {
            CommonLogger.info(TAG, "User connection state "
                    + chatUserInformation.getActorConnectionState());
            Toast.makeText(getActivity(), "The connection request has been sent\n you need to wait until the user responds", Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.btn_connection_request_reject) {
            CommonLogger.info(TAG, "User connection state "
                    + chatUserInformation.getActorConnectionState());
            Toast.makeText(getActivity(), "The connection request has been rejected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateButton() {
        try {
            connectionState
                    = moduleManager.getActorConnectionState(chatUserInformation.getActorPublickey());
        } catch (CantValidateActorConnectionStateException e) {
            e.printStackTrace();
        }
        switch (connectionState) {
            case BLOCKED_LOCALLY:
            case BLOCKED_REMOTELY:
            case CANCELLED_LOCALLY:
            case CANCELLED_REMOTELY:
                connectionRejected();
                break;
            case CONNECTED:
                disconnectRequest();
                break;
            case NO_CONNECTED:
            case DISCONNECTED_LOCALLY:
            case DISCONNECTED_REMOTELY:
            case ERROR:
            case DENIED_LOCALLY:
            case DENIED_REMOTELY:
                connectRequest();
                break;
            case PENDING_REMOTELY_ACCEPTANCE:
                connectionSend();
                break;

            case PENDING_LOCALLY_ACCEPTANCE:
                conectionAccept();
                break;
        }
    }


    private void connectionSend() {
        connectionRequestSend.setVisibility(View.VISIBLE);
        connect.setVisibility(View.GONE);
        disconnect.setVisibility(View.GONE);
        connectionRequestRejected.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
    }

    private void conectionAccept(){
        connectionRequestSend.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);
        disconnect.setVisibility(View.GONE);
        connectionRequestRejected.setVisibility(View.GONE);
        accept.setVisibility(View.VISIBLE);
        accept.setBackgroundResource(R.drawable.cht_comm_bg_shape_blue);
    }

    private void connectRequest() {
        connectionRequestSend.setVisibility(View.GONE);
        connect.setVisibility(View.VISIBLE);
        disconnect.setVisibility(View.GONE);
        connectionRequestRejected.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
    }

    private void disconnectRequest() {
        connectionRequestSend.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);
        disconnect.setVisibility(View.VISIBLE);
        connectionRequestRejected.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
    }

    private void connectionRejected() {
        connectionRequestSend.setVisibility(View.GONE);
        connect.setVisibility(View.GONE);
        disconnect.setVisibility(View.GONE);
        connectionRequestRejected.setVisibility(View.VISIBLE);
        accept.setVisibility(View.GONE);
    }

    private Drawable getImgDrawable(byte[] customerImg) {
        if (customerImg != null && customerImg.length > 0)
            return ImagesUtils.getRoundedBitmap(res, customerImg);

        return ImagesUtils.getRoundedBitmap(res, R.drawable.cht_comm_bg_circular_other_profile);//profile_image);
    }

    private void setUpScreen(LayoutInflater layoutInflater) throws CantGetChatUserIdentityException {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}

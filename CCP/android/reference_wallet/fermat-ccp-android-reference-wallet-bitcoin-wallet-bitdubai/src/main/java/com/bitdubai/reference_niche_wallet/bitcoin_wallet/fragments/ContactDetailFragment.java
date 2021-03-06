package com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.ReferenceAppFermatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.ErrorManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.interfaces.CryptoWalletWalletContact;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.popup.DeleteWalletContactDialog;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.popup.ReceiveFragmentDialog;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.utils.BitmapWorkerTask;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.session.SessionConstant;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Contact Detail Fragment.
 *
 * @author Francisco Vásquez
 * @version 1.0
 */
public class ContactDetailFragment extends AbstractFermatFragment<ReferenceAppFermatSession<CryptoWallet>,ResourceProviderManager> implements View.OnClickListener {


    /**
     * Root fragment view reference
     */
    private View mFragmentView;
    /**
     * Fragment UI controls
     */
    private ImageView image_view_profile;
    private EditText edit_text_name;
    private TextView text_view_address;
    private LinearLayout linear_layout_extra_user_receive;

    /**
     * Typeface Font
     */
    private Typeface typeface;
    /**
     * Platform
     */
    private CryptoWallet cryptoWallet;
    private String PublicKey;
    private ErrorManager errorManager;

    /**
     * DATA
     */
    private CryptoWalletWalletContact cryptoWalletWalletContact;
    /**
     *  Resources
     */
    WalletResourcesProviderManager walletResourcesProviderManager;

    private FermatButton send_button;
    private FermatButton receive_button;
    private ImageView img_update;
    private boolean addressIsTouch=false;
    private static final long DELAY_TIME = 2;
    private Handler delayHandler = new Handler();
    private Runnable delay = new Runnable() {
        @Override
        public void run() {
            addressIsTouch = false;
        }
    };
    private BlockchainNetworkType blockchainNetworkType;
    private ImageView img_copy;


    public static ContactDetailFragment newInstance() {
        ContactDetailFragment f = new ContactDetailFragment();
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setHasOptionsMenu(true);
            cryptoWalletWalletContact = (CryptoWalletWalletContact) appSession.getData("LastContactSelected");
            if(cryptoWalletWalletContact==null){
                onBack(null);
            }
            //typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CaviarDreams.ttf");
            errorManager = appSession.getErrorManager();
            cryptoWallet = appSession.getModuleManager();
            PublicKey = cryptoWallet.getSelectedActorIdentity().getPublicKey();
            if(appSession.getData(SessionConstant.BLOCKCHANIN_TYPE) != null)
                blockchainNetworkType = (BlockchainNetworkType)appSession.getData(SessionConstant.BLOCKCHANIN_TYPE);
            else
                blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();

        } catch (Exception e) {
            errorManager.reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_std_message), Toast.LENGTH_SHORT).show();
        }
//        /* Load Wallet Contact */
//        walletContact = CollectionUtils.find(getWalletContactList(), new Predicate<WalletContact>() {
//            @Override
//            public boolean evaluate(WalletContact walletContact) {
//                try {
//                    return walletContact.name.equalsIgnoreCase(accountName);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                return false;
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            mFragmentView = inflater.inflate(R.layout.contact_detail_base, container, false);

            setUp();
            setUpContact();
            return mFragmentView;
        }catch (Exception e){
            errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE,e);
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_std_message), Toast.LENGTH_SHORT).show();
        }
        return container;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        try{
            if ( id == R.id.send_button) {
                if(cryptoWalletWalletContact.getReceivedCryptoAddress().size() > 0) {
                    appSession.setData("LastContactSelected",cryptoWalletWalletContact);
                    appSession.setData(SessionConstant.FROM_ACTIONBAR_SEND_ICON_CONTACTS, false);
                    changeActivity(Activities.CCP_BITCOIN_WALLET_SEND_FORM_ACTIVITY, appSession.getAppPublicKey());
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.error_msg_no_address),Toast.LENGTH_SHORT).show();
                }
            }
            else if( id == R.id.receive_button){
                if(cryptoWalletWalletContact.getReceivedCryptoAddress().size() > 0) {
                    appSession.setData("LastContactSelected", cryptoWalletWalletContact);
                    appSession.setData(SessionConstant.FROM_ACTIONBAR_SEND_ICON_CONTACTS, false);
                    changeActivity(Activities.CCP_BITCOIN_WALLET_REQUEST_FORM_ACTIVITY, appSession.getAppPublicKey());
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.error_msg_no_address),Toast.LENGTH_SHORT).show();
                }
            }
            else if ( id == R.id.linear_layout_extra_user_receive){
                ReceiveFragmentDialog receiveFragmentDialog = new ReceiveFragmentDialog(
                        getActivity(),
                        cryptoWallet,
                        appSession.getErrorManager(),
                        cryptoWalletWalletContact,
                        PublicKey,
                        appSession.getAppPublicKey(),
                        blockchainNetworkType);
                receiveFragmentDialog.show();
            }

        }catch (Exception e){
            errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE,e);
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_std_message), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setup UI references
     */
    private void setUp() {
        if (mFragmentView != null) {
            image_view_profile = (ImageView) mFragmentView.findViewById(R.id.image_view_profile);
            edit_text_name = (EditText) mFragmentView.findViewById(R.id.edit_text_name);
            text_view_address = (TextView) mFragmentView.findViewById(R.id.text_view_address);
            receive_button = (FermatButton) mFragmentView.findViewById(R.id.receive_button);
            send_button = (FermatButton) mFragmentView.findViewById(R.id.send_button);
            img_copy = (ImageView) mFragmentView.findViewById(R.id.img_copy);
            linear_layout_extra_user_receive = (LinearLayout) mFragmentView.findViewById(R.id.linear_layout_extra_user_receive);
            img_update = (ImageView) mFragmentView.findViewById(R.id.img_update);
            send_button.setOnClickListener(this);
            receive_button.setOnClickListener(this);
            linear_layout_extra_user_receive.setOnClickListener(this);
            if (typeface != null) {
                edit_text_name.setTypeface(typeface);
                text_view_address.setTypeface(typeface);
                receive_button.setTypeface(typeface);
                send_button.setTypeface(typeface);
            }
            if(cryptoWalletWalletContact!=null)
            if(cryptoWalletWalletContact.getActorType().equals(Actors.INTRA_USER)){
                linear_layout_extra_user_receive.setVisibility(View.GONE);
            }
            img_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!addressIsTouch) {
                            addressIsTouch=true;
                            cryptoWallet.sendAddressExchangeRequest(
                                    cryptoWalletWalletContact.getActorName(),
                                    Actors.INTRA_USER,
                                    cryptoWalletWalletContact.getActorPublicKey(),
                                    cryptoWalletWalletContact.getProfilePicture(),
                                    Actors.INTRA_USER,
                                    PublicKey
                                    , appSession.getAppPublicKey(),
                                    CryptoCurrency.BITCOIN,
                                    blockchainNetworkType
                            );

                            delayHandler.postDelayed(delay, TimeUnit.MINUTES.toMillis(DELAY_TIME));
                        }else{
                            Toast.makeText(getActivity(),getResources().getString(R.string.success_msg_address_sent_wait),Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            img_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        setClipboard(getActivity(), text_view_address.getText().toString());
                        Toast.makeText(getActivity(),getResources().getString(R.string.success_msg_address_copied_clipboard),Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }




    /**
     * Setting up wallet contact value
     */
    private void setUpContact() {
        image_view_profile = (ImageView) mFragmentView.findViewById(R.id.image_view_profile);

        //if api 17 set address font size
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
            text_view_address.setTextSize(14);

        if (cryptoWalletWalletContact != null) {
            if(image_view_profile!=null){
                try {
                          //  Bitmap bitmapDrawable = BitmapFactory.decodeByteArray(cryptoWalletWalletContact.getProfilePicture(), 0, cryptoWalletWalletContact.getProfilePicture().length);// MemoryUtils.decodeSampledBitmapFromByteArray(cryptoWalletWalletContact.getProfilePicture(),image_view_profile.getMaxWidth(),image_view_profile.getMaxHeight());
                          //  bitmapDrawable = Bitmap.createScaledBitmap(bitmapDrawable, image_view_profile.getWidth(), 200, true);
                           // image_view_profile.setImageBitmap(bitmapDrawable);
                        BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(image_view_profile,getResources(),false);
                        bitmapWorkerTask.execute(cryptoWalletWalletContact.getProfilePicture());
                }catch (Exception e){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(),R.string.error_msg_loading_img,Toast.LENGTH_SHORT).show();

                    }
                    e.printStackTrace();
                }
            }
            if (edit_text_name != null)
                edit_text_name.setText(cryptoWalletWalletContact.getActorName());
            if (text_view_address != null) {
                if (cryptoWalletWalletContact.getReceivedCryptoAddress().size() > 0) {



                       try {


                           if (cryptoWalletWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType)== null){


                           cryptoWallet.sendAddressExchangeRequest(
                                   cryptoWalletWalletContact.getActorName(),
                                   Actors.INTRA_USER,
                                   cryptoWalletWalletContact.getActorPublicKey(),
                                   cryptoWalletWalletContact.getProfilePicture(),
                                   Actors.INTRA_USER,
                                   PublicKey,
                                   appSession.getAppPublicKey(),
                                   CryptoCurrency.BITCOIN,
                                   blockchainNetworkType
                           );

                           img_update.setVisibility(View.VISIBLE);
                           receive_button.setVisibility(View.GONE);
                           send_button.setVisibility(View.GONE);


                       }else{
                           String address = cryptoWalletWalletContact.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress();
                           //TODO: si la address es nula hay que ver porqué es
                           text_view_address.setText((address!=null)?address:"mnK7DuBQT3REr9bmfYcufTwjiAWfjwRwMf");
                           img_update.setVisibility(View.GONE);
                           receive_button.setVisibility(View.VISIBLE);
                           send_button.setVisibility(View.VISIBLE);
                       }

                       } catch (Exception e) {
                           e.printStackTrace();
                      /* } catch (NullPointerException e) {

                           try {
                               cryptoWallet.sendAddressExchangeRequest(
                                       cryptoWalletWalletContact.getActorName(),
                                       Actors.INTRA_USER,
                                       cryptoWalletWalletContact.getActorPublicKey(),
                                       cryptoWalletWalletContact.getProfilePicture(),
                                       Actors.INTRA_USER,
                                       cryptoWallet.getActiveIdentities().get(0).getPublicKey(),
                                       appSession.getAppPublicKey(),
                                       CryptoCurrency.BITCOIN,
                                       blockchainNetworkType
                               );
                           } catch (Exception e1) {
                               e1.printStackTrace();
                           }

                           img_update.setVisibility(View.VISIBLE);
                           receive_button.setVisibility(View.GONE);
                           send_button.setVisibility(View.GONE);*/


                       }




            }else{
                    img_update.setVisibility(View.VISIBLE);
                    receive_button.setVisibility(View.GONE);
                    send_button.setVisibility(View.GONE);
                }
            }else{
                text_view_address.setText("Waiting...");
                img_update.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Bitmap to byte[]
     *
     * @param bitmap Bitmap
     * @return byte array
     */
    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            if (id == 2){
                DeleteWalletContactDialog deleteWalletContactDialog = new DeleteWalletContactDialog(
                        getActivity(),
                        appSession,
                        null,
                        cryptoWalletWalletContact.getContactId(),
                        cryptoWalletWalletContact.getActorName());
                deleteWalletContactDialog.show();

                deleteWalletContactDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (appSession.getData(SessionConstant.CONTACT_DELETED) == true)
                            changeActivity(Activities.CWP_WALLET_RUNTIME_WALLET_BASIC_WALLET_BITDUBAI_VERSION_1_CONTACTS, appSession.getAppPublicKey());
                    }
                });
            }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Oooops! recovering from system error",
                    LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUpdateViewOnUIThread(String code){
      try
        {
            if(!code.equals("BlockchainDownloadComplete"))
            {

                //check contact to show is de same to update
                if(cryptoWalletWalletContact.getContactId().equals(UUID.fromString(code))){
                    //update contact address
                    CryptoWalletWalletContact cryptoWalletWalletContactUpdate = cryptoWallet.findWalletContactById(UUID.fromString(code), PublicKey);

                    if(cryptoWalletWalletContactUpdate.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress() != null)
                    {
                        cryptoWalletWalletContact = cryptoWalletWalletContactUpdate;
                        text_view_address.setText(cryptoWalletWalletContactUpdate.getReceivedCryptoAddress().get(blockchainNetworkType).getAddress());
                        img_update.setVisibility(View.GONE);
                        receive_button.setVisibility(View.VISIBLE);
                        send_button.setVisibility(View.VISIBLE);

                    }

                    appSession.setData("LastContactSelected", cryptoWalletWalletContactUpdate);
                }

            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    // copy text to clipboard
    private void setClipboard(Context context,String text) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("Address", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}

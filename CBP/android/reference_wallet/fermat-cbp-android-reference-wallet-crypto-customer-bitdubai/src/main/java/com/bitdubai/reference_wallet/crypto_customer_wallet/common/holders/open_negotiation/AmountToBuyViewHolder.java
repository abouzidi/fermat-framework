package com.bitdubai.reference_wallet.crypto_customer_wallet.common.holders.open_negotiation;

import android.view.View;
import android.widget.TextView;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatButton;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStepStatus;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.ClauseInformation;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.CustomerBrokerNegotiationInformation;
import com.bitdubai.reference_wallet.crypto_customer_wallet.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Map;


/**
 * Created by Yordin Alayn on 22.01.16.
 * Based in AmountToBuyViewHolder of Star_negotiation by nelson
 */
public class AmountToBuyViewHolder extends ClauseViewHolder implements View.OnClickListener {

    private TextView currencyToBuyTextValue;
    private TextView buyingText;
    private FermatButton buyingValue;
    private boolean paymentBuy;
    private View separatorLineUp;
    private View separatorLineDown;
    NumberFormat numberFormat = DecimalFormat.getInstance();

    public AmountToBuyViewHolder(View itemView) {
        super(itemView);

        this.paymentBuy = Boolean.TRUE;

        currencyToBuyTextValue = (TextView) itemView.findViewById(R.id.ccw_currency_to_buy);
        buyingText = (TextView) itemView.findViewById(R.id.ccw_buying_text);
        buyingValue = (FermatButton) itemView.findViewById(R.id.ccw_buying_value);
        separatorLineDown = itemView.findViewById(R.id.ccw_line_down);
        separatorLineUp = itemView.findViewById(R.id.ccw_line_up);
        buyingValue.setOnClickListener(this);
    }

    @Override
    public void bindData(CustomerBrokerNegotiationInformation data, ClauseInformation clause, int position) {
        super.bindData(data, clause, position);

        final Map<ClauseType, ClauseInformation> clauses = data.getClauses();

        ClauseType currencyType = ClauseType.CUSTOMER_CURRENCY;
        int buyingTextValue = R.string.buying_text;

        if (!paymentBuy) {
            currencyType = ClauseType.BROKER_CURRENCY;
            buyingTextValue = R.string.paying_text;
        }

        final ClauseInformation currencyToBuy = clauses.get(currencyType);

        currencyToBuyTextValue.setText(currencyToBuy.getValue());
        buyingText.setText(buyingTextValue);
        if (clause.getValue().equals("0.0") || clause.getValue().equals("0") || clause.getValue().equals("0,0")) {
            buyingValue.setText(defaultValue());
        } else {
            buyingValue.setText(fixFormat(clause.getValue()));
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClauseClicked(buyingValue, clause, clausePosition);
    }

    @Override
    public void setViewResources(int titleRes, int positionImgRes, int... stringResources) {
        titleTextView.setText(titleRes);
        clauseNumberImageView.setImageResource(positionImgRes);
    }

    @Override
    protected int getConfirmButtonRes() {
        return R.id.ccw_confirm_button;
    }

    @Override
    protected int getClauseNumberImageViewRes() {
        return R.id.ccw_clause_number;
    }

    @Override
    protected int getTitleTextViewRes() {
        return R.id.ccw_card_view_title;
    }

    @Override
    public void setStatus(NegotiationStepStatus clauseStatus) {
        super.setStatus(clauseStatus);

        switch (clauseStatus) {
            case ACCEPTED:
                buyingText.setTextColor(getColor(R.color.description_text_status_accepted));
                currencyToBuyTextValue.setTextColor(getColor(R.color.ccw_text_value_status_accepted));
                separatorLineDown.setBackgroundColor(getColor(R.color.card_title_color_status_accepted));
                separatorLineUp.setBackgroundColor(getColor(R.color.card_title_color_status_accepted));
                break;
            case CHANGED:
                separatorLineDown.setBackgroundColor(getColor(R.color.description_text_status_changed));
                separatorLineUp.setBackgroundColor(getColor(R.color.description_text_status_changed));
                buyingText.setTextColor(getColor(R.color.description_text_status_changed));
                currencyToBuyTextValue.setTextColor(getColor(R.color.description_text_status_changed));
                break;
            case CONFIRM:
                buyingText.setTextColor(getColor(R.color.description_text_status_confirm));
                currencyToBuyTextValue.setTextColor(getColor(R.color.text_value_status_confirm));
                break;
        }
    }


    private String fixFormat(String value) {

            if (compareLessThan1(value)) {
                numberFormat.setMaximumFractionDigits(8);
            } else {
                numberFormat.setMaximumFractionDigits(2);
            }
            return numberFormat.format(new BigDecimal(Double.valueOf(value)));

    }


    private Boolean compareLessThan1(String value) {
        return BigDecimal.valueOf(Double.valueOf(value)).compareTo(BigDecimal.ONE) == -1;
    }

    String defaultValue(){
        DecimalFormatSymbols symbols =((DecimalFormat)  numberFormat).getDecimalFormatSymbols();
        if(symbols.getDecimalSeparator()=='.'){
            return "0.0";
        }else{
            return "0,0";
        }
    }


    public boolean setPaymentBuy(boolean paymentBuy) {
        return this.paymentBuy = paymentBuy;
    }
}

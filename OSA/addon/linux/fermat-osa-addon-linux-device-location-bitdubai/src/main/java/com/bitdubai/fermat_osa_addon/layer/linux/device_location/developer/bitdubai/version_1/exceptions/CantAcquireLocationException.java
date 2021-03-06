package com.bitdubai.fermat_osa_addon.layer.linux.device_location.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * The exception <code>com.bitdubai.fermat_osa_addon.layer.linux.device_location.developer.bitdubai.version_1.exceptions.CantAcquireLocationException</code>
 * is thrown when there is an error trying to acquire the location of the device.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 01/04/2016.
 *
 * @author lnacosta
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantAcquireLocationException extends FermatException {

    private static final String DEFAULT_MESSAGE = "CAN'T ACQUIRE LOCATION EXCEPTION";

    public CantAcquireLocationException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantAcquireLocationException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

}

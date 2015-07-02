package com.bitdubai.fermat_dmp_plugin.layer.middleware.app_runtime.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.layer.dmp_middleware.app_runtime.exceptions.AppRuntimeExceptions;


/**
 * Created by natalia on 01/07/15.
 */
public class CantFactoryReset extends AppRuntimeExceptions {

    /**
     *
     */
    private static final long serialVersionUID = 4841032427648911456L;

    public static final String DEFAULT_MESSAGE = "CAN'T LOAD APP STRUCTURE";

    //private final String fileName;

    public CantFactoryReset(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantFactoryReset(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantFactoryReset(final String message) {
        this(message, null);
    }

    public CantFactoryReset(final Exception exception) {
        this(exception.getMessage());
        setStackTrace(exception.getStackTrace());
    }

    public CantFactoryReset() {
        this(DEFAULT_MESSAGE);
    }

}
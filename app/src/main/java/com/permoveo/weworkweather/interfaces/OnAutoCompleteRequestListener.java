package com.permoveo.weworkweather.interfaces;

import com.permoveo.weworkweather.models.AutoCompleteResult;

/**
 * Created by byfieldj on 3/21/16.
 */
public interface OnAutoCompleteRequestListener {

    public abstract void onAutoCompleteRequestComplete(AutoCompleteResult result);
    public abstract void onAutoCompleteRequestFailed();
}

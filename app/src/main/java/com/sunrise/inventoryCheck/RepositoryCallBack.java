package com.sunrise.inventoryCheck;

import com.sunrise.inventoryCheck.enums.CustomResponse;

public interface RepositoryCallBack {

    void onReadComplete(String result, CustomResponse response);
}

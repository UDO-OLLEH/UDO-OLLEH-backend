package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class MenuDuplicatedException extends RuntimeException{

    public MenuDuplicatedException(){
        super(ErrorCode.MENU_DUPLICATED.getMessage());
    }

    public MenuDuplicatedException(Exception ex){
        super(ex);
    }
}

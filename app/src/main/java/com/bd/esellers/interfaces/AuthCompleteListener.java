package com.bd.esellers.interfaces;

import com.bd.esellers.userAuth.userAuthModels.Token;

public interface AuthCompleteListener {
    void onAuthComplete(Token token);
}

package com.gorrotowi.parkemeter;

import com.simplify.android.sdk.model.Card;

/**
 * Created by gorro on 02/08/15.
 */
public enum SingletonCard {
    SINGLETON;

    public static Card card;
    public static String idUser;
    public static String mail;

    public static void setCard(Card newCard) {
        card = newCard;
    }

    public static Card getCard() {
        return card;
    }

    public static void setUserId(String id) {
        idUser = id;
    }

    public static String getUserId() {
        return idUser;
    }

    public static void setMail(String email) {
        mail = email;
    }

    public static String getMail() {
        return mail;
    }

}

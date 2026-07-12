package ro.ugal.aciee.bettingagency.ui;

import ro.ugal.aciee.bettingagency.model.Account;

public class Session {
    private static Account currentAccount;
    private static Runnable onChange;

    public static void login(Account account) {
        currentAccount = account;
        notifyChange();
    }

    public static void logout() {
        currentAccount = null;
        notifyChange();
    }

    public static Account getCurrentUser() {
        return currentAccount;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    public static void setOnChange(Runnable listener) {
        onChange = listener;
    }

    public static void notifyChange() {
        if (onChange != null) {
            onChange.run();
        }
    }
}
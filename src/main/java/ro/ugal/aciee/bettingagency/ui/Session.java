package ro.ugal.aciee.bettingagency.ui;

import ro.ugal.aciee.bettingagency.model.Account;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private static Account currentAccount;
    private static final List<Runnable> listeners = new ArrayList<>();

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

    public static void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public static void notifyChange() {
        for (Runnable listener : new ArrayList<>(listeners)) {
            listener.run();
        }
    }
}
package ro.ugal.aciee.bettingagency.ui;

import ro.ugal.aciee.bettingagency.model.Account;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private static Account currentAccount;
    private static final List<Runnable> accountChangeListeners = new ArrayList<>();
    private static final List<Runnable> authChangeListeners = new ArrayList<>();

    public static void login(Account account) {
        currentAccount = account;
        notifyAuthChange();
        notifyAccountChange();
    }

    public static void logout() {
        currentAccount = null;
        notifyAuthChange();
        notifyAccountChange();
    }

    public static void refreshCurrentUser(Account account) {
        currentAccount = account;
        notifyAccountChange();
    }

    public static Account getCurrentUser() {
        return currentAccount;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }

    public static void addAccountChangeListener(Runnable listener) {
        accountChangeListeners.add(listener);
    }

    public static void addAuthChangeListener(Runnable listener) {
        authChangeListeners.add(listener);
    }

    private static void notifyAccountChange() {
        for (Runnable listener : new ArrayList<>(accountChangeListeners)) {
            listener.run();
        }
    }

    private static void notifyAuthChange() {
        for (Runnable listener : new ArrayList<>(authChangeListeners)) {
            listener.run();
        }
    }
}
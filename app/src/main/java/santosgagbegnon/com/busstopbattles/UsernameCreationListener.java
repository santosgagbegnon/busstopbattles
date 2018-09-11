package santosgagbegnon.com.busstopbattles;

public interface UsernameCreationListener {
    void onSuccess();
    void onUsernameTakenFailure();
    void onUsernameTooShortFailure();
    void onIllegalCharacterFailure();
}

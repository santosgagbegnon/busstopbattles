package santosgagbegnon.com.busstopbattles;

import android.util.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.*;

public class UsernameSetupActivityModel {
    private static final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    private static final String VERIFIED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz1234567890._";
    private static final String TAG = "UsernameSetuModel";

    public static void addNewUser(final String newUsername, final UsernameCreationListener closure){
        if (newUsername == null || newUsername.length() < 3){
            closure.onUsernameTooShortFailure();
            return;
        }

        for(int character = 0; character < newUsername.length();character++){
            char characterAtIndex = Character.toLowerCase(newUsername.charAt(character));
            if (VERIFIED_CHARACTERS.indexOf(characterAtIndex) == -1){
                closure.onIllegalCharacterFailure();
                return;
            }
        }
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean usernameFound = false;
                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                while(usersIterator.hasNext()){
                    User currentUserInDatabase = usersIterator.next().getValue(User.class);
                    if (currentUserInDatabase != null && currentUserInDatabase.getUsername().toLowerCase().equals(newUsername.toLowerCase())) {
                        usernameFound = true;
                        closure.onUsernameTakenFailure();
                        break;
                    }
                }
                if (!usernameFound){
                    User newUser = new User(newUsername);
                    usersRef.child(newUsername).setValue(newUser);
                    closure.onSuccess();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Error when attempting to create account: " + databaseError.toString());
            }
        });
    }
}

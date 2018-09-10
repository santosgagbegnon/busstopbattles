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

    public static void addNewUser(final String newUsername, final WasSuccessful closure){
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean usernameFound = false;
                Iterator<DataSnapshot> usersIterator = dataSnapshot.getChildren().iterator();
                while (usersIterator.hasNext()){
                    User currentUser = usersIterator.next().getValue(User.class);
                    if (currentUser != null && currentUser.getUsername().equals(newUsername)) {
                        usernameFound = true;
                        closure.onFailure();
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

            }
        });


    }





}

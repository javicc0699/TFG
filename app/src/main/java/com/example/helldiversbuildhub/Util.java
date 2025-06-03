package com.example.helldiversbuildhub;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Util {

    /**
     * Crea un Map<String, Integer> uniendo dos arrays:
     * - uno de nombres (string-array)
     * - otro de iconos (typed-array con referencias a drawable)
     */
    public static Map<String, Integer> mapFromArrays(Context ctx, int namesArrayResId, int iconsArrayResId) {
        String[] names = ctx.getResources().getStringArray(namesArrayResId);
        TypedArray icons = ctx.getResources().obtainTypedArray(iconsArrayResId);

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            int drawableId = icons.getResourceId(i, 0);
            map.put(names[i], drawableId);
        }
        icons.recycle();
        return map;
    }

    public static void votarBuild(String buildId, String userId, String newVoteType) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference buildRef = db.collection("builds").document(buildId);
        DocumentReference voteRef = buildRef.collection("votes").document(userId);

        db.runTransaction(transaction -> {
                    // lee voto previo
                    DocumentSnapshot voteSnap = transaction.get(voteRef);
                    String oldVote = voteSnap.exists() ? voteSnap.getString("type") : null;

                    // si pulsa dos veces lo mismo → quita
                    if (oldVote != null && oldVote.equals(newVoteType)) {
                        transaction.update(buildRef,
                                "likes", FieldValue.increment(oldVote.equals("like") ? -1 : 0),
                                "dislikes", FieldValue.increment(oldVote.equals("dislike") ? -1 : 0)
                        );
                        transaction.delete(voteRef);
                        return null;
                    }

                    // si hay voto contrario, quítalo
                    if ("like".equals(oldVote)) {
                        transaction.update(buildRef, "likes", FieldValue.increment(-1));
                    } else if ("dislike".equals(oldVote)) {
                        transaction.update(buildRef, "dislikes", FieldValue.increment(-1));
                    }

                    // aplica el nuevo
                    transaction.update(buildRef,
                            "likes", FieldValue.increment(newVoteType.equals("like") ? 1 : 0),
                            "dislikes", FieldValue.increment(newVoteType.equals("dislike") ? 1 : 0)
                    );
                    transaction.set(voteRef, Collections.singletonMap("type", newVoteType));
                    return null;
                })
                .addOnSuccessListener(ok -> Log.d("Util", "Voto ok"))
                .addOnFailureListener(err -> Log.e("Util", "Error al votar", err));
    }

}

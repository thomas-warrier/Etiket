package exportkit;

import com.google.firebase.firestore.DocumentReference;

public interface OnGetMarketDocumentReference {
    void getMarketReference(DocumentReference docRef);
}

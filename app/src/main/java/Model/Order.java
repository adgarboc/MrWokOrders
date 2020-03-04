package Model;

import com.google.firebase.firestore.FieldValue;

public class Order
{
    private String uid;
    private String name;
    private Base base;
    private Protein protein;
    private boolean attended;
    private FieldValue timestamp;

    public Order() {
    }

    public Order(String uid, String name, Base base, Protein protein, boolean attended, FieldValue timestamp) {
        this.uid = uid;
        this.name = name;
        this.base = base;
        this.protein = protein;
        this.attended = attended;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Protein getProtein() {
        return protein;
    }

    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public FieldValue getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(FieldValue timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

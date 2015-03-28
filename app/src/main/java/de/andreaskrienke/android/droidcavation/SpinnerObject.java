package de.andreaskrienke.android.droidcavation;

/**
 * Generic Spinner Object class.
 *
 * Retrieve ID to store in DB:
 * <pre>
 * int databaseId = Integer.parseInt(((SpinnerObject) spin2.getSelectedItem()).getId());
 * </pre>
 */
public class SpinnerObject {

    private  int databaseId;
    private String databaseValue;

    public SpinnerObject ( int databaseId , String databaseValue ) {
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    // Use @Override to avoid accidental overloading.
    @Override public boolean equals(Object o) {
        // Return true if the objects are identical.
        // (This is just an optimization, not required for correctness.)
        if (this == o) {
            return true;
        }

        // Return false if the other object has the wrong type.
        // This type may be an interface depending on the interface's specification.
        if (!(o instanceof SpinnerObject)) {
            return false;
        }

        // Cast to the appropriate type.
        // This will succeed because of the instanceof, and lets us access private fields.
        SpinnerObject lhs = (SpinnerObject) o;

        // Check each field. Primitive fields, reference fields, and nullable reference
        // fields are all treated differently.
        return databaseId == lhs.databaseId ;
    }

    public int getId () {
        return databaseId;
    }

    public String getValue () {
        return databaseValue;
    }

    @Override
    public String toString () {
        return databaseValue;
    }

}
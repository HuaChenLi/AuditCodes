package src.Panels;

public class AuditAccountID {
    int id;
    String description;

    public AuditAccountID(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return description;
    }
}

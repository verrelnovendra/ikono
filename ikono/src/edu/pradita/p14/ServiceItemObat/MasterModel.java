package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MasterModel {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty quantity;

    public MasterModel(String id, String name, String quantity) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleStringProperty(quantity);
    }

    // Getter dan Setter untuk ID
    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    // Getter dan Setter untuk Name
    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Getter dan Setter untuk Quantity
    public StringProperty quantityProperty() {
        return quantity;
    }

    public String getQuantity() {
        return quantity.get();
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }
}

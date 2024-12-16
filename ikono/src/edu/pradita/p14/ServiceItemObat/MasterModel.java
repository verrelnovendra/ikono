package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MasterModel {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final StringProperty price;
    private final StringProperty quantity;

    public MasterModel(String id, String name, String category, String price, String quantity) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleStringProperty(price);
        this.quantity = new SimpleStringProperty(quantity);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty priceProperty() {
        return price;
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

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

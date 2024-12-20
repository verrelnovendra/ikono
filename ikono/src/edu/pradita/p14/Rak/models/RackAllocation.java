package models;

public class RackAllocation {
    private int rackId;
    private int productId;
    private int quantityOnHand;

    // Getters and Setters
    public int getRackId() { return rackId; }
    public void setRackId(int rackId) { this.rackId = rackId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(int quantityOnHand) { this.quantityOnHand = quantityOnHand; }
}

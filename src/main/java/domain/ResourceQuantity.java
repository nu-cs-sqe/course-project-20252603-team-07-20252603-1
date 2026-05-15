package domain;

public class ResourceQuantity {
    private final Resource resource;
    private final int quantity;

    public ResourceQuantity(Resource resource, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        if (resource == Resource.DESERT) {
            throw new IllegalArgumentException("Resource must be tradeable.");
        }
        this.resource = resource;
        this.quantity = quantity;
    }

    public Resource getResource() {
        return resource;
    }

    public int getQuantity() {
        return quantity;
    }
}

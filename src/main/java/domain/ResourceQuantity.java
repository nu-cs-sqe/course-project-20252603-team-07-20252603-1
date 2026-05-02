package domain;

public class ResourceQuantity {
    public ResourceQuantity(Resource resource, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        if (resource == Resource.DESERT) {
            throw new IllegalArgumentException("Resource must be tradeable.");
        }
    }
}

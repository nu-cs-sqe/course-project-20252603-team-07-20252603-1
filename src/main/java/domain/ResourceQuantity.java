package domain;

public class ResourceQuantity {
    public ResourceQuantity(Resource resource, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
    }
}

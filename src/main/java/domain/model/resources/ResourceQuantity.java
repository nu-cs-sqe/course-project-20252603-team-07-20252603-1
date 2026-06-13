package domain.model.resources;

/** Immutable value object representing a quantity of a specific resource. */
public class ResourceQuantity {

  private final Resource resource;
  private final int quantity;

  private ResourceQuantity(Resource resource, int quantity) {
    this.resource = resource;
    this.quantity = quantity;
  }

  /**
   * Creates a new ResourceQuantity with validation.
   *
   * @param resource the resource type (must not be DESERT)
   * @param quantity the amount (must be at least 1)
   * @return a new ResourceQuantity
   */
  public static ResourceQuantity create(Resource resource, int quantity) {
    if (quantity < 1) {
      throw new IllegalArgumentException("Quantity must be at least 1.");
    }
    if (resource == Resource.DESERT) {
      throw new IllegalArgumentException("Resource must be tradeable.");
    }
    return new ResourceQuantity(resource, quantity);
  }

  /**
   * Returns the resource type.
   *
   * @return the resource
   */
  public Resource getResource() {
    return resource;
  }

  /**
   * Returns the quantity.
   *
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }
}

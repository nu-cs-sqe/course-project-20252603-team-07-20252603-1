package domain.model.resources;

public class ResourceCard {

    private ResourceType type;

    public ResourceCard(ResourceType type) {
        this.type = type;
    }


    public ResourceType getType() {

        return this.type;

    }
    
}

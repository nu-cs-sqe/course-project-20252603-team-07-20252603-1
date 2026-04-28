package domain.model.resources;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {

    @Test
    void testConstructorAndGetter() {
        ResourceCard woodCard = new ResourceCard(ResourceType.WOOD);
        assertEquals(ResourceType.WOOD, woodCard.getType());

        ResourceCard rockCard = new ResourceCard(ResourceType.ROCK);
        assertEquals(ResourceType.ROCK, rockCard.getType());

        ResourceCard sheepCard = new ResourceCard(ResourceType.SHEEP);
        assertEquals(ResourceType.SHEEP, sheepCard.getType());

        ResourceCard clayCard = new ResourceCard(ResourceType.CLAY);
        assertEquals(ResourceType.CLAY, clayCard.getType());

        ResourceCard wheatCard = new ResourceCard(ResourceType.WHEAT);
        assertEquals(ResourceType.WHEAT, wheatCard.getType());
    }
}

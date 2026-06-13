package domain.model.board;

import domain.model.player.Player;
import domain.model.resources.Resource;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents a hexagonal tile on the game board, tracking its resource type and occupants. */
public class Hex {
  private static final int MIN_HEX_ID = 0;
  private static final int MAX_HEX_ID = 18;

  private static final int MIN_ROLL_NUMBER = 2;
  private static final int MAX_ROLL_NUMBER = 12;
  private static final int DESERT_ROLL_NUMBER = 7;

  private static final int MAX_BUILDINGS_ON_HEX = 3;
  private static final int SETTLEMENT_RESOURCE_AMOUNT = 1;
  private static final int CITY_RESOURCE_AMOUNT = 2;

  private final int hexId;
  private final Resource resource;
  private final int hexRollNum;
  private List<Player> playerSettlements;
  private List<Player> playerCities;
  private int totalBuildingsOnHex;

  @SuppressFBWarnings(
      value = "CT_CONSTRUCTOR_THROW",
      justification = "Validation in constructor is intentional; no finalizer risk")
  Hex(int hexId, Resource resource, int rollNumber) {
    validateHexId(hexId);
    this.hexId = hexId;
    validateRollNum(rollNumber);
    this.hexRollNum = rollNumber;
    validateResourceAndHexNumber(resource, rollNumber);
    this.resource = resource;
    this.playerSettlements = new ArrayList<>();
    this.playerCities = new ArrayList<>();
    this.totalBuildingsOnHex = 0;
  }

  private void validateHexId(int hexId) {
    if (hexId < MIN_HEX_ID || hexId > MAX_HEX_ID) {
      throw new IllegalArgumentException("Invalid Hex - hexId must be within [0, 18].");
    }
  }

  private void validateRollNum(int rollNumber) {
    if (rollNumber < MIN_ROLL_NUMBER || rollNumber > MAX_ROLL_NUMBER) {
      throw new IllegalArgumentException(
          "Invalid Hex - rollNumber must be within [2, 12].");
    }
  }

  private void validateResourceAndHexNumber(Resource resource, int rollNum) {
    if (resource != Resource.DESERT && rollNum == DESERT_ROLL_NUMBER) {
      throw new IllegalArgumentException(
          "Invalid Hex - Only Desert Hex can have rollNumber 7");
    } else if (resource == Resource.DESERT && rollNum != DESERT_ROLL_NUMBER) {
      throw new IllegalArgumentException(
          "Invalid Hex - Desert Hex must have rollNumber 7.");
    }
  }

  void addPlayerSettlementToHex(Player player) {
    if (this.totalBuildingsOnHex >= MAX_BUILDINGS_ON_HEX) {
      throw new IllegalStateException("Already three buildings on hex.");
    } else if (player == null) {
      throw new IllegalArgumentException("Adding invalid player name to Hex.");
    } else {
      playerSettlements.add(player);
      this.totalBuildingsOnHex++;
    }
  }

  void removePlayerSettlementFromHex(Player player) {
    boolean success = playerSettlements.remove(player);
    if (!success) {
      throw new IllegalArgumentException("Player does not have a settlement on hex.");
    } else {
      this.totalBuildingsOnHex--;
    }
  }

  void addPlayerCityToHex(Player player) {
    if (this.totalBuildingsOnHex >= MAX_BUILDINGS_ON_HEX) {
      throw new IllegalStateException("Already three buildings on hex.");
    } else if (player == null) {
      throw new IllegalArgumentException("Adding invalid player name to Hex.");
    } else {
      playerCities.add(player);
      this.totalBuildingsOnHex++;
    }
  }

  void awardSettlementResources() {
    if (resource != Resource.DESERT) {
      playerSettlements.forEach(player -> {
        player.updateResources(resource, SETTLEMENT_RESOURCE_AMOUNT);
      });
    }
  }

  void awardCityResources() {
    if (resource != Resource.DESERT) {
      playerCities.forEach(player -> {
        player.updateResources(resource, CITY_RESOURCE_AMOUNT);
      });
    }
  }

  int getSettlementCount() {
    return playerSettlements.size();
  }

  int getCityCount() {
    return playerCities.size();
  }

  int getHexId() {
    return this.hexId;
  }

  Resource getHexResource() {
    return this.resource;
  }

  int getHexRollNum() {
    return this.hexRollNum;
  }

  boolean isPlayerSettlementOnHex(Player player) {
    return playerSettlements.contains(player);
  }

  boolean isPlayerCityOnHex(Player player) {
    return playerCities.contains(player);
  }

  public List<Player> getHexSettlementPlayers() {
    return Collections.unmodifiableList(playerSettlements);
  }

  public List<Player> getHexCityPlayers() {
    return Collections.unmodifiableList(playerCities);
  }
}

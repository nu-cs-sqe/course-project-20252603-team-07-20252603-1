package ui.controller;

/**
 * Result codes for adding a player during game setup.
 */
public enum PlayerAddResult {
  SUCCESS,
  NAME_EMPTY,
  NAME_TAKEN,
  COLOR_EMPTY,
  COLOR_TAKEN
}

package ui.view.board;

/**
 * The interaction mode of the {@link BoardView}: what kind of board element,
 * if any, the user is currently being asked to pick.
 */
public enum BoardSelectionMode {
  INERT,
  PICK_NODE,
  PICK_EDGE,
  PICK_HEX
}

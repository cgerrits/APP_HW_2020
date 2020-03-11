package Trees;

public enum Order {
    /**
     * Represents an order to print a tree
     * PRE: current, left, right
     * IN: left, current, right
     * POST: left, right, current
     */
    PRE, IN, POST
}

package animator;

/**
 * Command represents an action that can be undo'd and redo'd.
 * 
 * @author Neill Johnston
 */
public interface Command {
    /**
     * Simple undo.
     */
    public void undo();

    /**
     * Simple redo.
     */
    public void redo();
}

package core;

/**
 * Interface for a container that holds comments during collecting like buffer.
 * Use flush() to commit the comments to the storage and clear the container.
 * This helps to decrease IO operations and avoid memory overflow.
 *
 * Implement this interface to add comments to the container.
 * Implement this interface to flush the comments to the storage.
 */
public interface ICommentsContainer {

  void addData(Comment newComment);

  void flush();
}

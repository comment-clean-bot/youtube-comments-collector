package core;

/**
 * Interface for a processor that commits the comment to the storage.
 *
 * Implement this interface to commit the comment to the storage.
 */
public interface ICommentProcessor {

  /**
   * Commit the data to the storage.
   * Storage can be a database, a file, a remote server, etc.
   * Implement this method to commit the data to proper the storage.
   *
   * @param comment the comment to commit
   */
  void commitData(Comment comment);
}

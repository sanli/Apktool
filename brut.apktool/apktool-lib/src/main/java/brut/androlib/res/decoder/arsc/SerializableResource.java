package brut.androlib.res.decoder.arsc;

import java.io.IOException;

/**
 * A resource, typically a @{link Chunk}, that can be converted to an array of bytes.
 */
public interface SerializableResource {
  
  /**
   * Converts this resource into an array of bytes representation.
   * @return An array of bytes representing this resource.
   * @throws IOException
   */
  byte[] toByteArray() throws IOException;

  /**
   * Converts this resource into an array of bytes representation.
   * @param shrink True if, when converting to a byte array, this resource can modify the returned
   *               bytes in an effort to reduce the size.
   * @return An array of bytes representing this resource.
   * @throws IOException
   */
  byte[] toByteArray(boolean shrink) throws IOException;
}

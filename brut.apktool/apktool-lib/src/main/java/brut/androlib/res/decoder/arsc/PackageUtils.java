package brut.androlib.res.decoder.arsc;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/** Provides utility methods for package names. */
public final class PackageUtils {

  public static final int PACKAGE_NAME_SIZE = 256;

  private PackageUtils() {}  // Prevent instantiation

  /**
   * Reads the package name from the buffer and repositions the buffer to point directly after
   * the package name.
   * @param buffer The buffer containing the package name.
   * @param offset The offset in the buffer to read from.
   * @return The package name.
   */
  public static String readPackageName(ByteBuffer buffer, int offset) {
    Charset utf16 = Charset.forName("UTF-16LE");
    String str = new String(buffer.array(), offset, PACKAGE_NAME_SIZE, utf16);
    buffer.position(offset + PACKAGE_NAME_SIZE);
    return str;
  }

  /**
   * Writes the provided package name to the buffer in UTF-16.
   * @param buffer The buffer that will be written to.
   * @param packageName The package name that will be written to the buffer.
   */
  public static void writePackageName(ByteBuffer buffer, String packageName) {
    buffer.put(packageName.getBytes(Charset.forName("UTF-16LE")), 0, PACKAGE_NAME_SIZE);
  }
}

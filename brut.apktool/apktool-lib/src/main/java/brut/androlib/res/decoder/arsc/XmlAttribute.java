package brut.androlib.res.decoder.arsc;

//import com.google.auto.value.AutoValue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** Represents an XML attribute and value. */
//@AutoValue
public abstract class XmlAttribute implements SerializableResource {

  /** The serialized size in bytes of an {@link XmlAttribute}. */
  public static final int SIZE = 12 + ResourceValue.SIZE;

  /** A string reference to the namespace URI, or -1 if not present. */
  public abstract int namespaceIndex();

  /** A string reference to the attribute name. */
  public abstract int nameIndex();

  /** A string reference to a string containing the character value.  */
  public abstract int rawValueIndex();

  /** A {@link ResourceValue} instance containing the parsed value. */
  public abstract ResourceValue typedValue();

  /** The parent of this XML attribute; used for dereferencing the namespace and name. */
  public abstract XmlNodeChunk parent();

  /** The namespace URI, or the empty string if not present. */
  public final String namespace() {
    return getString(namespaceIndex());
  }

  /** The attribute name, or the empty string if not present. */
  public final String name() {
    return getString(nameIndex());
  }

  /** The raw character value. */
  public final String rawValue() {
    return getString(rawValueIndex());
  }

  /**
   * Creates a new {@link XmlAttribute} based on the bytes at the current {@code buffer} position.
   *
   * @param buffer A buffer whose position is at the start of a {@link XmlAttribute}.
   * @param parent The parent chunk that contains this attribute; used for string lookups.
   */
  public static XmlAttribute create(ByteBuffer buffer, XmlNodeChunk parent) {
    int namespace = buffer.getInt();
    int name = buffer.getInt();
    int rawValue = buffer.getInt();
    ResourceValue typedValue = ResourceValue.create(buffer);
//    return new AutoValue_XmlAttribute(namespace, name, rawValue, typedValue, parent);
    throw new MigrationCodePlacement();
  }

  private String getString(int index) {
    return parent().getString(index);
  }

  @Override
  public byte[] toByteArray() {
    return toByteArray(false);
  }

  @Override
  public byte[] toByteArray(boolean shrink) {
    ByteBuffer buffer = ByteBuffer.allocate(SIZE).order(ByteOrder.LITTLE_ENDIAN);
    buffer.putInt(namespaceIndex());
    buffer.putInt(nameIndex());
    buffer.putInt(rawValueIndex());
    buffer.put(typedValue().toByteArray(shrink));
    return buffer.array();
  }

  /**
   * Returns a brief description of this XML attribute. The representation of this information is
   * subject to change, but below is a typical example:
   *
   * <pre>"XmlAttribute{namespace=foo, name=bar, value=1234}"</pre>
   */
  @Override
  public String toString() {
    return String.format("XmlAttribute{namespace=%s, name=%s, value=%s}",
        namespace(), name(), rawValue());
  }
}

package brut.androlib.res.decoder.arsc;

//import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of package-id to package name mappings for any shared libraries used in this
 * {@link ResourceTableChunk}. The package-id's encoded in this resource table may be different
 * than the id's assigned at runtime
 */
public final class LibraryChunk extends Chunk {

  /** The number of resources of this type at creation time. */
  private final int entryCount;

  /** The libraries used in this chunk (package id + name). */
  private final List<Entry> entries = new ArrayList<>();

  protected LibraryChunk(ByteBuffer buffer, @Nullable Chunk parent) {
    super(buffer, parent);
    entryCount = buffer.getInt();
  }

  @Override
  protected void init(ByteBuffer buffer) {
    super.init(buffer);
    entries.addAll(enumerateEntries(buffer));
  }

  private List<Entry> enumerateEntries(ByteBuffer buffer) {
    List<Entry> result = new ArrayList<>(entryCount);
    int offset = this.offset + getHeaderSize();
    int endOffset = offset + Entry.SIZE * entryCount;

    while (offset < endOffset) {
      result.add(Entry.create(buffer, offset));
      offset += Entry.SIZE;
    }
    return result;
  }

  @Override
  protected Type getType() {
    return Chunk.Type.TABLE_LIBRARY;
  }

  @Override
  protected void writeHeader(ByteBuffer output) {
    super.writeHeader(output);
    output.putInt(entries.size());
  }

  @Override
  protected void writePayload(DataOutput output, ByteBuffer header, boolean shrink)
      throws IOException {
    for (Entry entry : entries) {
      output.write(entry.toByteArray(shrink));
    }
  }

  /** A shared library package-id to package name entry. */
//  @AutoValue
  protected abstract static class Entry implements SerializableResource {

    /** Library entries only contain a package ID (4 bytes) and a package name. */
    private static final int SIZE = 4 + PackageUtils.PACKAGE_NAME_SIZE;

    /** The id assigned to the shared library at build time. */
    public abstract int packageId();

    /** The package name of the shared library. */
    public abstract String packageName();

    static Entry create(ByteBuffer buffer, int offset) {
      int packageId = buffer.getInt(offset);
      String packageName = PackageUtils.readPackageName(buffer, offset + 4);
//      return new AutoValue_LibraryChunk_Entry(packageId, packageName);
      throw new MigrationCodePlacement();
    }

    @Override
    public byte[] toByteArray() throws IOException {
      return toByteArray(false);
    }

    @Override
    public byte[] toByteArray(boolean shrink) throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(SIZE).order(ByteOrder.LITTLE_ENDIAN);
      buffer.putInt(packageId());
      PackageUtils.writePackageName(buffer, packageName());
      return buffer.array();
    }
  }
}
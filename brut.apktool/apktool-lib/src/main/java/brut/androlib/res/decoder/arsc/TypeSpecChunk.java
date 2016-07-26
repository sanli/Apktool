package brut.androlib.res.decoder.arsc;

import com.google.common.primitives.UnsignedBytes;

import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/** A chunk that contains a collection of resource entries for a particular resource data type. */
public final class TypeSpecChunk extends Chunk {

  /** The id of the resource type that this type spec refers to. */
  private final int id;

  /** Resource configuration masks. */
  private final int[] resources;

  protected TypeSpecChunk(ByteBuffer buffer, @Nullable Chunk parent) {
    super(buffer, parent);
    id = UnsignedBytes.toInt(buffer.get());
    buffer.position(buffer.position() + 3);  // Skip 3 bytes for packing
    int resourceCount = buffer.getInt();
    resources = new int[resourceCount];

    for (int i = 0; i < resourceCount; ++i) {
      resources[i] = buffer.getInt();
    }
  }

  /**
   * Returns the (1-based) type id of the resources that this {@link TypeSpecChunk} has
   * configuration masks for.
   */
  public int getId() {
    return id;
  }

  /** Returns the number of resource entries that this chunk has configuration masks for. */
  public int getResourceCount() {
    return resources.length;
  }

  @Override
  protected Type getType() {
    return Chunk.Type.TABLE_TYPE_SPEC;
  }

  @Override
  protected void writeHeader(ByteBuffer output) {
    super.writeHeader(output);
    // id is an unsigned byte in the range [0-255]. It is guaranteed to be non-negative.
    // Because our output is in little-endian, we are making use of the 4 byte packing here
    output.putInt(id);
    output.putInt(resources.length);
  }

  @Override
  protected void writePayload(DataOutput output, ByteBuffer header, boolean shrink)
      throws IOException {
    for (int resource : resources) {
      output.writeInt(resource);
    }
  }
}

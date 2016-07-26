package brut.androlib.res.decoder.arsc;

import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an XML resource map chunk.
 *
 * <p>This chunk maps attribute ids to the resource ids of the attribute resource that defines the
 * attribute (e.g. type, enum values, etc.).
 */
public class XmlResourceMapChunk extends Chunk {

  /** The size of a resource reference for {@code resources} in bytes. */
  private static final int RESOURCE_SIZE = 4;

  /**
   * Contains a mapping of attributeID to resourceID. For example, the attributeID 2 refers to the
   * resourceID returned by {@code resources.get(2)}.
   */
  private final List<Integer> resources = new ArrayList<>();

  protected XmlResourceMapChunk(ByteBuffer buffer, @Nullable Chunk parent) {
    super(buffer, parent);
  }

  @Override
  protected void init(ByteBuffer buffer) {
    super.init(buffer);
    resources.addAll(enumerateResources(buffer));
  }

  private List<Integer> enumerateResources(ByteBuffer buffer) {
    int resourceCount = (getOriginalChunkSize() - getHeaderSize()) / RESOURCE_SIZE;
    List<Integer> result = new ArrayList<>(resourceCount);
    int offset = this.offset + getHeaderSize();
    buffer.mark();
    buffer.position(offset);

    for (int i = 0; i < resourceCount; ++i) {
      result.add(buffer.getInt());
    }

    buffer.reset();
    return result;
  }

  /** Returns the resource ID that this {@code attributeId} maps to. */
  public ResourceIdentifier getResourceId(int attributeId) {
    return ResourceIdentifier.create(resources.get(attributeId));
  }

  @Override
  protected Type getType() {
    return Chunk.Type.XML_RESOURCE_MAP;
  }

  @Override
  protected void writePayload(DataOutput output, ByteBuffer header, boolean shrink)
      throws IOException {
    super.writePayload(output, header, shrink);
    for (Integer resource : resources) {
      output.writeInt(resource);
    }
  }
}

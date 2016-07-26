package brut.androlib.res.decoder.arsc;

import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/** Represents a chunk whose payload is a list of sub-chunks. */
public abstract class ChunkWithChunks extends Chunk {

  private final Map<Integer, Chunk> chunks = new LinkedHashMap<>();

  protected ChunkWithChunks(ByteBuffer buffer, @Nullable Chunk parent) {
    super(buffer, parent);
  }

  @Override
  protected void init(ByteBuffer buffer) {
    super.init(buffer);
    chunks.clear();
    int start = this.offset + getHeaderSize();
    int offset = start;
    int end = this.offset + getOriginalChunkSize();
    int position = buffer.position();
    buffer.position(start);

    while (offset < end) {
      Chunk chunk = Chunk.newInstance(buffer, this);
      chunks.put(offset, chunk);
      offset += chunk.getOriginalChunkSize();
    }

    buffer.position(position);
  }

  /**
   * Retrieves the @{code chunks} contained in this chunk.
   *
   * @return map of buffer offset -> chunk contained in this chunk.
   */
  public final Map<Integer, Chunk> getChunks() {
    return chunks;
  }

  @Override
  protected void writePayload(DataOutput output, ByteBuffer header, boolean shrink)
      throws IOException {
    for (Chunk chunk : getChunks().values()) {
      byte[] chunkBytes = chunk.toByteArray(shrink);
      output.write(chunkBytes);
      writePad(output, chunkBytes.length);
    }
  }
}

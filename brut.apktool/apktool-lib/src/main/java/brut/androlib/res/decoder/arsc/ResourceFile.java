package brut.androlib.res.decoder.arsc;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Given an arsc file, maps the contents of the file. */
public final class ResourceFile implements SerializableResource {

  /** The chunks contained in this resource file. */
  private final List<Chunk> chunks = new ArrayList<>();

  public ResourceFile(byte[] buf) {
    ByteBuffer buffer = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
    while (buffer.remaining() > 0) {
      chunks.add(Chunk.newInstance(buffer));
    }
  }

  /**
   * Given an input stream, reads the stream until the end and returns a {@link ResourceFile}
   * representing the contents of the stream.
   *
   * @param is The input stream to read from.
   * @return ResourceFile represented by the @{link InputStream}.
   * @throws IOException
   */
  public static ResourceFile fromInputStream(InputStream is) throws IOException {
    byte[] buf = ByteStreams.toByteArray(is);
    return new ResourceFile(buf);
  }

  /** Returns the chunks in this resource file. */
  public List<Chunk> getChunks() {
    return Collections.unmodifiableList(chunks);
  }

  @Override
  public byte[] toByteArray() throws IOException {
    return toByteArray(false);
  }

  @Override
  public byte[] toByteArray(boolean shrink) throws IOException {
    ByteArrayDataOutput output = ByteStreams.newDataOutput();
    for (Chunk chunk : chunks) {
      output.write(chunk.toByteArray(shrink));
    }
    return output.toByteArray();
  }
}

package top.mrxiaom.bili.live.runtime.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class PacketHeader {
    public static final int KPacketHeaderLength = 16;

    public int packetLength;
    public short headerLength;
    public ProtocolVersion protocolVersion;
    public Operation operation;
    public int sequenceId;

    public int getBodyLength() {
        return packetLength - headerLength;
    }

    public PacketHeader() {

    }

    public PacketHeader(byte[] b) {
        packetLength = readInt32BigEndian(b, 0, 4);
        headerLength = readInt16BigEndian(b, 4, 6);
        protocolVersion = ProtocolVersion.valueOf(readInt16BigEndian(b, 6, 8));
        operation = Operation.valueOf(readInt32BigEndian(b, 8, 12));
        sequenceId = readInt32BigEndian(b, 12, 16);
    }

    public byte[] toBytes() {
        return getBytes(packetLength, headerLength, protocolVersion, operation, sequenceId);
    }

    /**
     * 生成弹幕协议的头部
     */
    public static byte[] getBytes(int packetLength, short headerLength, ProtocolVersion protocolVersion, Operation operation) {
        return getBytes(packetLength, headerLength, protocolVersion, operation, 1);
    }

    /**
     * 生成弹幕协议的头部
     *
     * @param packetLength    消息数据包长度
     * @param headerLength    头部长度
     * @param protocolVersion 弹幕协议版本
     * @param operation       数据包操作
     * @param sequenceId      序列号
     */
    public static byte[] getBytes(int packetLength, short headerLength, ProtocolVersion protocolVersion, Operation operation, int sequenceId) {
        var bytes = new byte[KPacketHeaderLength];
        writeInt32BigEndian(bytes, 0, 4, packetLength);
        writeInt16BigEndian(bytes, 4, 6, headerLength);
        writeInt16BigEndian(bytes, 6, 8, (short) protocolVersion.value);
        writeInt32BigEndian(bytes, 8, 12, operation.value);
        writeInt32BigEndian(bytes, 12, 16, sequenceId);
        return bytes;
    }

    public static short readInt16BigEndian(byte[] bytes, int start, int end) {
        byte[] fin = Arrays.copyOfRange(bytes, start, end);
        ByteBuffer wrap = ByteBuffer.wrap(fin);
        wrap.order(ByteOrder.BIG_ENDIAN);
        return wrap.getShort();
    }

    public static int readInt32BigEndian(byte[] bytes, int start, int end) {
        byte[] fin = Arrays.copyOfRange(bytes, start, end);
        ByteBuffer wrap = ByteBuffer.wrap(fin);
        wrap.order(ByteOrder.BIG_ENDIAN);
        return wrap.getInt();
    }

    public static void writeInt16BigEndian(byte[] bytes, int start, int end, short value) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(value);
        byte[] fin = buffer.array();
        for (int i = start, j = 0;
             i < bytes.length && i < end && j < fin.length;
             i++, j++) {
            bytes[i] = fin[j];
        }
    }

    public static void writeInt32BigEndian(byte[] bytes, int start, int end, int value) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(value);
        byte[] fin = buffer.array();
        for (int i = start, j = 0;
             i < bytes.length && i < end && j < fin.length;
             i++, j++) {
            bytes[i] = fin[j];
        }
    }

    /**
     * 操作数据
     */
    public enum Operation {
        /**
         * 心跳包
         */
        HeartBeat(2),

        /**
         * 服务器心跳回应(包含人气信息)
         */
        HeartBeatResponse(3),

        /**
         * 服务器消息(正常消息)
         */
        ServerNotify(5),

        /**
         * 客户端认证请求
         */
        Authority(7),

        /**
         * 认证回应
         */
        AuthorityResponse(8);

        public final int value;

        Operation(int value) {
            this.value = value;
        }

        public static Operation valueOf(int value) {
            for (Operation o : values()) {
                if (o.value == value) return o;
            }
            return null;
        }
    }

    /**
     * 弹幕协议版本
     */
    public enum ProtocolVersion {
        /**
         * 未压缩数据
         */
        UnCompressed(0),

        /**
         * 心跳数据
         */
        HeartBeat(1),

        /**
         * zlib数据
         */
        Zlib(2),

        /**
         * Br数据
         */
        Brotli(3);

        public final int value;

        ProtocolVersion(int value) {
            this.value = value;
        }

        public static ProtocolVersion valueOf(int value) {
            for (ProtocolVersion p : values()) {
                if (p.value == value) return p;
            }
            return null;
        }
    }
}

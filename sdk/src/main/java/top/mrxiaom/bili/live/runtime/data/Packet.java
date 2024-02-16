package top.mrxiaom.bili.live.runtime.data;

import top.mrxiaom.bili.live.runtime.data.PacketHeader.Operation;
import top.mrxiaom.bili.live.runtime.data.PacketHeader.ProtocolVersion;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Packet {
    private static final Packet s_NoBodyHeartBeatPacket;

    static {
        s_NoBodyHeartBeatPacket = new Packet(Operation.HeartBeat);
        {
            s_NoBodyHeartBeatPacket.header = new PacketHeader();
            {
                s_NoBodyHeartBeatPacket.header.headerLength = PacketHeader.KPacketHeaderLength;
                s_NoBodyHeartBeatPacket.header.sequenceId = 1;
                s_NoBodyHeartBeatPacket.header.protocolVersion = ProtocolVersion.HeartBeat;
                s_NoBodyHeartBeatPacket.header.operation = Operation.HeartBeat;
            }
        }
    }

    ;
    public PacketHeader header;

    public int length() {
        return header.packetLength;
    }

    public byte[] packetBody;

    public Packet() {
        header = new PacketHeader();
    }

    public Packet(byte[] bytes) {
        byte[] headerArr = Arrays.copyOfRange(bytes, 0, PacketHeader.KPacketHeaderLength);
        header = new PacketHeader(headerArr);
        packetBody = Arrays.copyOfRange(bytes, PacketHeader.KPacketHeaderLength, bytes.length);
    }

    public Packet(Operation operation) {
        this(operation, null);
    }

    public Packet(Operation operation, byte[] body) {
        header = new PacketHeader();
        header.operation = operation;
        header.protocolVersion = ProtocolVersion.UnCompressed;
        header.packetLength = PacketHeader.KPacketHeaderLength + (body != null ? body.length : 0);
        packetBody = body;
    }

    public byte[] toBytes() {
        if (packetBody != null)
            header.packetLength = header.headerLength + packetBody.length;
        else
            header.packetLength = header.headerLength;
        byte[] arr = new byte[header.packetLength];

        byte[] headerArr = header.toBytes();
        for (int i = 0; i < headerArr.length && i < header.headerLength; i++) {
            arr[i] = headerArr[i];
        }

        if (packetBody != null) {
            int total = header.headerLength + packetBody.length;
            for (int i = header.headerLength, j = 0;
                 i < total && j < packetBody.length;
                 i++, j++) {
                arr[i] = packetBody[j];
            }
        }
        return arr;
    }

    /**
     * 生成心跳包
     */
    public static Packet heartbeat() {
        return heartbeat((byte[]) null);
    }

    /**
     * 生成附带msg信息的心跳包
     */
    public static Packet heartbeat(String msg) {
        return heartbeat(msg.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成附带msg信息的心跳包
     */
    public static Packet heartbeat(byte[] msg) {
        if (msg == null) return s_NoBodyHeartBeatPacket;
        Packet packet = new Packet();

        packet.header.packetLength = PacketHeader.KPacketHeaderLength + msg.length;
        packet.header.protocolVersion = ProtocolVersion.HeartBeat;
        packet.header.operation = Operation.HeartBeat;
        packet.header.sequenceId = 1;
        packet.header.headerLength = PacketHeader.KPacketHeaderLength;

        packet.packetBody = msg;
        return packet;
    }

    public static Packet authority(String token) {
        return authority(token, ProtocolVersion.HeartBeat);
    }

    public static Packet authority(String token, ProtocolVersion protocolVersion) {
        byte[] obj = token.getBytes(StandardCharsets.UTF_8);

        Packet packet = new Packet();

        packet.header.operation = Operation.Authority;
        packet.header.protocolVersion = protocolVersion;
        packet.header.sequenceId = 1;
        packet.header.headerLength = PacketHeader.KPacketHeaderLength;
        packet.header.packetLength = PacketHeader.KPacketHeaderLength + obj.length;

        packet.packetBody = obj;
        return packet;
    }
}

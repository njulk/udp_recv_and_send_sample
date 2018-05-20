package nju.lemon;

import nju.lemon.exceptions.MessageTooLongException;
import nju.lemon.utils.ByteUtils;

import java.io.Serializable;

public class MyPacket implements Serializable {
    private int srcId;
    private int destId;
    private long time;
    private MyLocation location;
    private String message;

    public MyPacket() {
        this(0, 0, 0, 0, 0);
    }

    public MyPacket(int srcId, int destId, long time, double latitude, double longitude) {
        this(srcId, destId, time, latitude, longitude, null);
    }

    public MyPacket(int srcId, int destId, long time, double latitude, double longitude, String message) {
        if (message != null && message.length() > 990) {
            throw new MessageTooLongException("max message length is 990, yours is " + message.length());
        }
        this.srcId = srcId;
        this.destId = destId;
        this.time = time;
        this.location = new MyLocation();
        this.location.latitude = latitude;
        this.location.longitude = longitude;
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (message == null) {
            //No message, send the newest location info
            hash = srcId + 31 * hash;
        } else {
            //Have message, packet specified by src, dest, time and message;
            hash = srcId + 31 * hash;
            hash = destId + 31 * hash;
            hash = (int) time + 31 * hash;          //TODO: this seems not suitable
            hash = message.hashCode() + hash;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MyPacket)) return false;

        MyPacket other = (MyPacket) obj;

        if (other.message == null && this.message == null) {
            return this.srcId == other.srcId;
        } else {
            return this.srcId == other.srcId &&
                    this.destId == other.destId &&
                    this.time == other.time &&
                    this.message.equals(other.message);
        }
    }

    public byte[] getBytes() {
        byte[] buffer = new byte[26 + message.length()];
        //src and dest
        buffer[0] = (byte) (0xff & srcId);
        buffer[1] = (byte) (0xff & destId);
        System.arraycopy(ByteUtils.longToBytes(time), 0, buffer, 2, 8);  //2-9
        System.arraycopy(ByteUtils.doubleToBytes(location.latitude), 0, buffer, 10, 8);  //10-17
        System.arraycopy(ByteUtils.doubleToBytes(location.longitude), 0, buffer, 18, 8); //18-25
        System.arraycopy(message.getBytes(), 0, buffer, 26, message.length());
        return buffer;
    }

    public static MyPacket fromBytes(byte[] bytes) {
        int srcId = (bytes[0] + 256) % 256;
        int destId = (bytes[1] + 256) % 256;
        byte[] buf = new byte[8];
        System.arraycopy(bytes, 2, buf, 0, 8);
        long time = ByteUtils.bytesToLong(buf);
        System.arraycopy(bytes, 10, buf, 0, 8);
        double latitude = ByteUtils.bytesToDouble(buf);
        System.arraycopy(bytes, 18, buf, 0, 8);
        double longitude = ByteUtils.bytesToDouble(buf);
        String message = new String(bytes, 26, bytes.length - 26);
        return new MyPacket(srcId, destId, time, latitude, longitude, message);
    }

    @Override
    public String toString() {
        //Method for debug
        return "==========================================================================\n" +
                "Message (" + srcId + " --> " + destId + ")\n" +
                "\tTime: " + time + ". Location: (" + location.toString() + ")\n" +
                "\tMessage: " + message + "\n";
    }

    class MyLocation {
        double latitude;
        double longitude;

        @Override
        public String toString() {
            return "" + latitude + ", " + longitude;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) return true;
            if(!(obj instanceof MyLocation)) return false;
            MyLocation other = (MyLocation)obj;
            return other.longitude == this.longitude &&
                    other.latitude == this.latitude;
        }
    }
}

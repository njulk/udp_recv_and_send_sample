package nju.lemon;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

public class MyPacketTest {
    @Test
    public void testPacketEquals() {
        String message = "test message";
        MyPacket packet = new MyPacket(1, 2, 1000, 100.1, 200.2, message);
        MyPacket same = new MyPacket(1, 2, 1000, 100.1, 200.2, message);
        MyPacket noMsg1 = new MyPacket(1, 0, 100, 100.1, 200.2);
        MyPacket noMsg2 = new MyPacket(1, 0, 200, 100.3, 200.4);
        MyPacket noMsg3 = new MyPacket(2, 0, 200, 100.3, 200.4);
        Set<MyPacket> set = new LinkedHashSet<>();
        set.add(packet);
        set.add(same);
        set.add(noMsg1);
        set.add(noMsg2);
        set.add(noMsg3);
        Assert.assertEquals(3, set.size());
    }

    @Test
    public void testGetBytes() {
        String message = "test message";
        MyPacket packet = new MyPacket(1, 2, 1000, 100.1, 200.2, message);
        System.out.println(packet.toString());
        MyPacket from = MyPacket.fromBytes(packet.getBytes());
        Assert.assertEquals(packet, from);
    }
}

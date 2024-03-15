package com.action35.guncraft.custom_data_types;

import java.nio.ByteBuffer;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class GunDataType implements PersistentDataType<byte[], GunData> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<GunData> getComplexType() {
        return GunData.class;
    }

    @Override
    public byte[] toPrimitive(GunData complex, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[19], 0, 19);
        bb.put(complex.reloadTime);
        bb.putShort(complex.shotTime);
        bb.putShort(complex.magazineSize);
        bb.putShort(complex.ammo);
        bb.putShort(complex.damage);
        bb.putLong(complex.timestamp);
        bb.putShort(complex.distance);
        return bb.array();
    }

    @Override
    public GunData fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        byte reload = bb.get();
        short shotTime = bb.getShort();
        short mag = bb.getShort();
        short ammo = bb.getShort();
        short damage = bb.getShort();
        long timestamp = bb.getLong();
        short distance = bb.getShort();
        return new GunData(reload, shotTime, mag, ammo, damage, timestamp, distance);
    }
}

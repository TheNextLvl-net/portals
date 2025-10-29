package net.thenextlvl.portals.adapter.shape;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.portals.shape.Cuboid;
import net.thenextlvl.portals.shape.Cylinder;
import net.thenextlvl.portals.shape.Ellipsoid;
import net.thenextlvl.portals.shape.Shape;
import net.thenextlvl.portals.shape.Sphere;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ShapeAdapter implements TagAdapter<Shape> {
    private final CuboidAdapter cuboidAdapter = new CuboidAdapter();
    private final SphereAdapter sphereAdapter = new SphereAdapter();
    private final EllipsoidAdapter ellipsoidAdapter = new EllipsoidAdapter();
    private final CylinderAdapter cylinderAdapter = new CylinderAdapter();

    @Override
    public Shape deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var type = root.get("type").getAsString();
        var shape = root.get("shape");
        return (switch (type) {
            case "cuboid" -> cuboidAdapter;
            case "sphere" -> sphereAdapter;
            case "ellipsoid" -> ellipsoidAdapter;
            case "cylinder" -> cylinderAdapter;
            default -> throw new ParserException("Unknown shape type: " + type);
        }).deserialize(shape, context);
    }

    @Override
    public Tag serialize(Shape shape, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("type", switch (shape) {
            case Cuboid cuboid -> "cuboid";
            case Sphere sphere -> "sphere";
            case Ellipsoid ellipsoid -> "ellipsoid";
            case Cylinder cylinder -> "cylinder";
            default -> throw new ParserException("Unknown shape object: " + shape.getClass().getName());
        });
        tag.add("shape", (switch (shape) {
            case Cuboid cuboid -> cuboidAdapter.serialize(cuboid, context);
            case Sphere sphere -> sphereAdapter.serialize(sphere, context);
            case Ellipsoid ellipsoid -> ellipsoidAdapter.serialize(ellipsoid, context);
            case Cylinder cylinder -> cylinderAdapter.serialize(cylinder, context);
            default -> throw new ParserException("Unknown shape object: " + shape.getClass().getName());
        }));
        return tag;
    }
}

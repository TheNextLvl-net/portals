package net.thenextlvl.portals.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.math.Position;
import net.kyori.adventure.text.Component;
import net.thenextlvl.portals.Portal;
import net.thenextlvl.portals.PortalsPlugin;
import net.thenextlvl.portals.event.EntityPortalEnterEvent;
import net.thenextlvl.portals.event.EntityPortalExitEvent;
import net.thenextlvl.portals.event.PreEntityPortalEnterEvent;
import net.thenextlvl.portals.shape.BoundingBox;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class PortalListener implements Listener {
    private final PortalsPlugin plugin;
    private final Map<Portal, Map<UUID, Instant>> lastEntry = new HashMap<>();
    private final Map<UUID, Portal> lastPortal = new HashMap<>();

    public PortalListener(PortalsPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean hasCooldown(Portal portal, Entity entity) {
        var entries = this.lastEntry.get(portal);
        if (entries == null) return false;
        var lastEntry = entries.get(entity.getUniqueId());
        return lastEntry != null && Instant.now().isBefore(lastEntry.plus(portal.getCooldown()));
    }

    private void setLastEntry(Portal portal, Entity entity) {
        lastEntry.computeIfAbsent(portal, ignored -> new HashMap<>())
                .put(entity.getUniqueId(), Instant.now());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityMove(EntityMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (processMovement(event.getEntity(), event.getTo())) return;
        pushBack(event.getEntity());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (processMovement(event.getPlayer(), event.getTo())) return;
        pushBack(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) return;
        if (processMovement(event.getPlayer(), event.getPlayer().getLocation())) return;
        pushBack(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityPortalEnter(org.bukkit.event.entity.EntityPortalEnterEvent event) {
        plugin.portalProvider().getPortals(event.getLocation().getWorld())
                .filter(portal -> portal.getBoundingBox().contains(event.getLocation()))
                .findAny().ifPresent(portal -> event.setCancelled(true));
    }

    // todo: remove, just for testing
    @EventHandler(priority = EventPriority.MONITOR)
    public void onExit(EntityPortalExitEvent event) {
        System.out.println("Entity " + event.getEntity().getName() + " exited portal " + event.getPortal().getName());
    }

    // todo: remove, just for testing
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnter(EntityPortalEnterEvent event) {
        System.out.println("Entity " + event.getEntity().getName() + " entered portal " + event.getPortal().getName());
    }

    // todo: remove, just for testing
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPerEnter(PreEntityPortalEnterEvent event) {
        System.out.println("Entity " + event.getEntity().getName() + " is trying to enter portal " + event.getPortal().getName());
    }

    // todo: remove, just for testing
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPortalCreate(PortalCreateEvent event) {
        var maxX = event.getBlocks().stream().map(BlockState::getX).max(Integer::compare).orElse(0) + 1;
        var maxY = event.getBlocks().stream().map(BlockState::getY).max(Integer::compare).orElse(0) + 1;
        var maxZ = event.getBlocks().stream().map(BlockState::getZ).max(Integer::compare).orElse(0) + 1;
        var minX = event.getBlocks().stream().map(BlockState::getX).min(Integer::compare).orElse(0);
        var minY = event.getBlocks().stream().map(BlockState::getY).min(Integer::compare).orElse(0);
        var minZ = event.getBlocks().stream().map(BlockState::getZ).min(Integer::compare).orElse(0);
        var portal = plugin.portalProvider().createPortal(UUID.randomUUID().toString(), BoundingBox.cuboid(event.getWorld(), Position.fine(minX, minY, minZ), Position.fine(maxX, maxY, maxZ)));
        portal.setEntryAction((portal1, entity) -> {
            entity.sendMessage(Component.text("You entered the portal: " + portal1.getName()));
        });
        portal.setEntryPermission("nope.nope");
        plugin.getServer().broadcast(Component.text("created new portal: " + portal));
    }

    private boolean processMovement(Entity entity, Location to) {
        var boundingBox = translate(entity.getBoundingBox(), to);
        return plugin.portalProvider().getPortals(entity.getWorld())
                .filter(portal -> portal.getBoundingBox().overlaps(boundingBox))
                .findAny().map(portal -> {
                    if (portal.equals(lastPortal.get(entity.getUniqueId()))) return true;
                    if (!new PreEntityPortalEnterEvent(portal, entity).callEvent()) return false;
                    if (!portal.getEntryPermission().map(entity::hasPermission).orElse(true)) return false;
                    if (portal.getCooldown().isPositive() && hasCooldown(portal, entity)) return false;
                    if (portal.getEntryCost() > 0 && !withdrawEntryCost(portal, entity)) return false;
                    if (!new EntityPortalEnterEvent(portal, entity).callEvent()) return false;
                    portal.getEntryAction().ifPresent(action -> action.onEntry(portal, entity));
                    if (portal.getCooldown().isPositive()) setLastEntry(portal, entity);
                    lastPortal.put(entity.getUniqueId(), portal);
                    return true;
                }).orElseGet(() -> {
                    var removed = lastPortal.remove(entity.getUniqueId());
                    if (removed != null) new EntityPortalExitEvent(removed, entity).callEvent();
                    return true;
                });
    }

    private org.bukkit.util.BoundingBox translate(org.bukkit.util.BoundingBox boundingBox, Location location) {
        var widthX = boundingBox.getWidthX() / 2;
        var widthZ = boundingBox.getWidthZ() / 2;

        return new org.bukkit.util.BoundingBox(
                location.getX() - widthX, location.getY(), location.getZ() - widthZ,
                location.getX() + widthX, location.getY() + boundingBox.getHeight(), location.getZ() + widthZ
        );
    }

    private void pushBack(Entity entity) {
        entity.setVelocity(entity.getVelocity().multiply(-.1));
    }

    private boolean withdrawEntryCost(Portal portal, Entity entity) {
        // return entity.withdrawMoney(portal.getEntryCost()); // vault integration
        return true;
    }
}

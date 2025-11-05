import org.jspecify.annotations.NullMarked;

@NullMarked
module net.thenextlvl.portals {
    exports net.thenextlvl.portals.action.teleport;
    exports net.thenextlvl.portals.action;
    exports net.thenextlvl.portals.config;
    exports net.thenextlvl.portals.event;
    exports net.thenextlvl.portals.selection;
    exports net.thenextlvl.portals.shape;
    exports net.thenextlvl.portals;

    requires core.paper;
    requires net.kyori.adventure.key;
    requires org.bukkit;

    requires static org.jetbrains.annotations;
    requires static org.jspecify;
}
import org.jspecify.annotations.NullMarked;

@NullMarked
module net.thenextlvl.portals {
    exports net.thenextlvl.portals.action;
    exports net.thenextlvl.portals.bounds;
    exports net.thenextlvl.portals.event;
    exports net.thenextlvl.portals.notification;
    exports net.thenextlvl.portals.selection;
    exports net.thenextlvl.portals.shape;
    exports net.thenextlvl.portals.view;
    exports net.thenextlvl.portals;

    requires net.kyori.adventure.key;
    requires net.kyori.adventure.text.minimessage;
    requires net.kyori.adventure;
    requires net.kyori.examination.api;
    requires net.thenextlvl.binder;
    requires org.bukkit;

    requires static org.jetbrains.annotations;
    requires static org.jspecify;
}
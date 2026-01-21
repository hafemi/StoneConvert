package io.hafemi.stoneconvert;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ConvertCommand extends AbstractPlayerCommand {
    public ConvertCommand(@NonNullDecl String name, @NonNullDecl String description, boolean requiresConfirmation) {
        super(name, description, requiresConfirmation);
    }

    RequiredArg<String> messageArg = this.withRequiredArg("type", "Target Convert Type", ArgTypes.STRING);

    @Override
    protected void execute(
            @NonNullDecl CommandContext commandContext,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl PlayerRef playerRef,
            @NonNullDecl World world
    ) {
        final String convertType = messageArg.get(commandContext).toLowerCase();
        final Player player = getPlayer(store, playerRef);
        if (player != null) {
            switch(convertType) {
                case "stone":
                    updateInventory("Rock_Stone", "Rock_Stone_Cobble", player);
                    break;
                case "cobblestone", "cobble":
                    updateInventory("Rock_Stone_Cobble", "Rock_Stone", player);
                    break;
                default:
                    sendInvalidTypeMessage(playerRef, convertType);
                    return;
            }

            sendSuccessMessage(playerRef);
        }
    }

    @Nullable
    private Player getPlayer(Store<EntityStore> store, PlayerRef playerRef) {
        final Ref<EntityStore> playerRefEntity = playerRef.getReference();
        if (playerRefEntity == null) return null;

        return store.getComponent(
                playerRefEntity,
                Player.getComponentType()
        );
    }

    private void updateInventory(
            String fromType,
            String toType,
            Player player
    ) {
        final Inventory inventory = player.getInventory();
        final List<ItemContainer> itemContainers = List.of(
                inventory.getStorage(),
                inventory.getBackpack(),
                inventory.getHotbar()
        );

        for (ItemContainer container: itemContainers) {
            container.forEach((slot, itemStack) -> {
                final Item item = itemStack.getItem();
                final String blockId = item.getBlockId();
                if (blockId != null && blockId.equals(fromType)) {
                    final ItemStack newItemStack = new ItemStack(
                            toType,
                            itemStack.getQuantity()
                    );
                    container.setItemStackForSlot(slot, newItemStack);
                }
            });
        }

        inventory.markChanged();
    }

    private void sendInvalidTypeMessage(PlayerRef playerRef, String convertType) {
        final Message message = Message.join(
                Message.raw(convertType + " is an invalid type. \n").color(Color.red),
                Message.raw("Valid formats are cobblestone, cobble and stone").color(Color.RED)
        );

        playerRef.sendMessage(message);
    }

    private void sendSuccessMessage(PlayerRef playerRef) {
        final Message message = Message.raw("Successful Convert!").color(Color.green);

        playerRef.sendMessage(message);
    }
}

package ru.jtcf.tutorial.block.metalpress;

import ru.jtcf.tutorial.setup.ModBlocks;
import ru.jtcf.tutorial.setup.ModContainerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MetalPressContainer extends AbstractContainerMenu {
    private final ContainerData fields;

    private final ContainerLevelAccess container_access;

    // Client constructor
    public MetalPressContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, new ItemStackHandler(2), BlockPos.ZERO, new SimpleContainerData(buffer.readByte()));
    }

    // Server constructor
    public MetalPressContainer(int id, Inventory playerInventory, IItemHandler slots, BlockPos pos, ContainerData fields) {
        super(ModContainerTypes.METAL_PRESS.get(), id);
        this.container_access = ContainerLevelAccess.create(playerInventory.player.level, pos);
        this.fields = fields;

        this.addSlot(new SlotItemHandler(slots, 0, 56, 35));
        this.addSlot(new SlotItemHandler(slots, 1, 117, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Backpack
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = x + y * 9;
                int posX = 8 + x * 18;
                int posY = 84 + y * 18;
                this.addSlot(new Slot(playerInventory, index + 9, posX, posY));
            }
        }

        // Hotbar
        for (int x = 0; x < 9; ++x) {
            int posX = 8 + x * 18;
            int posY = 142;
            this.addSlot(new Slot(playerInventory, x, posX, posY));
        }

        addDataSlot(DataSlot.forContainer(fields, 0));
        addDataSlot(DataSlot.forContainer(fields, 1));
    }

    public static MenuConstructor getServerContainer(MetalPressBlockEntity metal_press, BlockPos pos) {
        return (id, playerInv, player) -> new MetalPressContainer(id, playerInv, metal_press.items, pos, metal_press.fields);
    }

    public int getProgressArrowScale() {
        int progress = fields.get(0);
        if (progress > 0) {
            return progress * 24 / MetalPressBlockEntity.WORK_TIME;
        }
        return 0;
    }

    public int getEnergyIndicatorScale() {
        int energyStored = fields.get(1);
        if (energyStored > 0) {
            return energyStored * 50 / MetalPressBlockEntity.MAX_ENERGY;
        }
        return 0;
    }

	@Override
    public boolean stillValid(Player player) {
        return stillValid(container_access, player, ModBlocks.METAL_PRESS.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();

            final int inventorySize = 2;
            final int playerHotbarEnd = inventorySize + 9;
            final int playerInventoryEnd = playerHotbarEnd + 27;

            if (index == 1) {
                if (!this.moveItemStackTo(itemStack1, inventorySize, playerInventoryEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (index != 0) {
                if (!this.moveItemStackTo(itemStack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, inventorySize, playerInventoryEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
        }

        return itemStack;
    }
}

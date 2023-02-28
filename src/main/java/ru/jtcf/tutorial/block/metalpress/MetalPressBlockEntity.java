package ru.jtcf.tutorial.block.metalpress;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import ru.jtcf.tutorial.crafting.recipe.PressingRecipe;
import ru.jtcf.tutorial.setup.ModBlockEntityTypes;
import ru.jtcf.tutorial.setup.ModRecipes;

import javax.annotation.Nullable;

// I have been told to not use the vanilla container classes and inetrfaces, but I think it's ok.
public class MetalPressBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, BlockEntityTicker<MetalPressBlockEntity> {
    static final int WORK_TIME = 2 * 20;
    static final int MAX_ENERGY = 10000;
    static final int MAX_TRANSFER = 100;

    public ItemStackHandler items;
    private final LazyOptional<? extends IItemHandler>[] handlers;

    private int progress = 0;

    public final EnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyHandler;

    public final ContainerData fields = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> energyStorage.getEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> {
                    int energy_to_receive = value - energyStorage.getEnergyStored();
                    while (energy_to_receive != 0) {
                        energy_to_receive -= energyStorage.receiveEnergy(energy_to_receive, false);
                    }

                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    };

    public MetalPressBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.METAL_PRESS.get(), pos, state);
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                super.onContentsChanged(slot);
            }
        };
        this.energyStorage = new EnergyStorage(MAX_ENERGY, MAX_TRANSFER);
        this.energyHandler = LazyOptional.of(() -> this.energyStorage);
    }

    void encodeExtraData(FriendlyByteBuf buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, MetalPressBlockEntity block_entity) {
        if (level.isClientSide) {
            return;
        }

        PressingRecipe recipe = block_entity.getRecipe();
        if (recipe != null) {
            block_entity.doWork(recipe);
        } else {
            block_entity.stopWork();
        }
    }

    @Nullable
    public PressingRecipe getRecipe() {
        if (this.level == null || getItem(0).isEmpty() || getEnergyStored() == 0) {
            return null;
        }
        return this.level.getRecipeManager().getRecipeFor(ModRecipes.Types.PRESSING.get(), this, this.level).orElse(null);
    }

    private ItemStack getWorkOutput(@Nullable PressingRecipe recipe) {
        if (recipe != null) {
            return recipe.assemble(this);
        }
        return ItemStack.EMPTY;
    }

    private void doWork(PressingRecipe recipe) {
        assert this.level != null;

        ItemStack current = getItem(1);
        ItemStack output = getWorkOutput(recipe);

        if (!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();

            if (!ItemStack.isSame(current, output) || newCount > output.getMaxStackSize()) {
                stopWork();
                return;
            }
        }

        if (progress < WORK_TIME) {
            ++progress;
        }

        if (progress >= WORK_TIME) {
            finishWork(recipe, current, output);
        }
    }

    private void finishWork(PressingRecipe recipe, ItemStack current, ItemStack output) {
        if (!current.isEmpty()) {
            current.grow(output.getCount());
        } else {
            setItem(1, output);
        }

        progress = 0;
        energyStorage.extractEnergy(recipe.getEnergyRequired(), false);
        this.removeItem(0, recipe.getIngredientCount());
        setChanged();
    }

    private void stopWork() {
        progress = 0;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.tutorial.metal_press");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerinventory) {
        return new MetalPressContainer(id, playerinventory, items, worldPosition, fields);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty() && getItem(1).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return this.items.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.items.extractItem(index, 1, false);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.setStackInSlot(index, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.level != null
                && this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.items.getSlots(); i++) {
            this.items.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void load(CompoundTag tags) {
        super.load(tags);
        this.items.deserializeNBT(tags.getCompound("Inventory"));

        this.progress = tags.getInt("Progress");
        int energy_to_load = tags.getInt("Energy") - energyStorage.getEnergyStored();
        while (energy_to_load != 0) {
            energy_to_load -= energyStorage.receiveEnergy(energy_to_load, false);
        }
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tags) {
        super.saveAdditional(tags);
        tags.put("Inventory", this.items.serializeNBT());
        tags.putInt("Progress", this.progress);
        tags.putInt("Energy", this.energyStorage.getEnergyStored());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag tags = this.getUpdateTag();
        tags.put("Inventory", this.items.serializeNBT());
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tags = super.getUpdateTag();
        tags.putInt("Progress", this.progress);
        tags.putInt("Energy", this.energyStorage.getEnergyStored());
        return tags;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP) {
                return this.handlers[0].cast();
            } else if (side == Direction.DOWN) {
                return this.handlers[1].cast();
            } else {
                return this.handlers[2].cast();
            }
        } else if (!this.remove && cap == CapabilityEnergy.ENERGY) {
            return energyHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
        energyHandler.invalidate();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
        energyHandler.invalidate();
    }
}

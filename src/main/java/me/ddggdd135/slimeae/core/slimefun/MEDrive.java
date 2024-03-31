package me.ddggdd135.slimeae.core.slimefun;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import me.ddggdd135.slimeae.api.MEItemCellStorage;
import me.ddggdd135.slimeae.api.MEStorageCellCache;
import me.ddggdd135.slimeae.api.StorageCollection;
import me.ddggdd135.slimeae.api.interfaces.IMEStorageObject;
import me.ddggdd135.slimeae.api.interfaces.IStorage;
import me.ddggdd135.slimeae.core.NetworkInfo;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MEDrive extends SlimefunItem implements IMEStorageObject, InventoryBlock {
    public static final int[] Boarder_Slots = new int[] {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 24, 25, 26, 27, 28, 29, 33, 34, 35, 36, 37, 38,
        42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53
    };
    public static final int[] MEItemStorageCell_Slots = new int[] {12, 13, 14, 21, 22, 23, 30, 31, 32, 39, 40, 41};

    public MEDrive(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        createPreset(this, this.getItem().getItemMeta().getDisplayName(), this::constructMenu);
        addItemHandler(onBlockBreak());
    }

    @Override
    public void onNetworkUpdate(Block block, NetworkInfo networkInfo) {}

    @Nonnull
    private BlockBreakHandler onBlockBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(Block b) {
                BlockMenu inv = StorageCacheUtils.getMenu(b.getLocation());

                if (inv != null) {
                    inv.dropItems(b.getLocation(), MEItemStorageCell_Slots);
                }
            }
        };
    }

    @OverridingMethodsMustInvokeSuper
    protected void constructMenu(BlockMenuPreset preset) {
        for (int slot : Boarder_Slots) {
            preset.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nullable @Override
    public IStorage getStorage(Block block) {
        BlockMenu inv = StorageCacheUtils.getMenu(block.getLocation());
        StorageCollection storageCollection = new StorageCollection();
        for (int slot : MEItemStorageCell_Slots) {
            ItemStack itemStack = inv.getItemInSlot(slot);
            if (itemStack != null
                    && !itemStack.getType().isAir()
                    && SlimefunItem.getByItem(itemStack) instanceof MEItemStorageCell) {
                storageCollection.addStorage(MEItemStorageCell.getStorage(itemStack));
            }
        }
        return storageCollection;
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }
}

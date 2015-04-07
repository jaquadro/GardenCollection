package com.jaquadro.minecraft.gardenstuff.integration.minetweaker;

import com.jaquadro.minecraft.gardenapi.api.GardenAPI;
import com.jaquadro.minecraft.gardenapi.api.machine.StandardCompostMaterial;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gardenstuff.CompostBin")
public class CompostBin
{
    @ZenMethod
    public static void add (IItemStack item) {
        add(item, 150);
    }

    @ZenMethod
    public static void add (IItemStack item, int processTime) {
        ItemStack stack = MineTweakerMC.getItemStack(item);
        if (stack != null && stack.getItem() != null)
            MineTweakerAPI.apply(new AddItemAction(stack, processTime));
        else
            MineTweakerAPI.logError("Tried to add invalid item to compost table.");
    }

    @ZenMethod
    public static void add (String oredictKey) {
        add(oredictKey, 150);
    }

    @ZenMethod
    public static void add (String oredictKey, int processTime) {
        if (oredictKey != null && oredictKey.length() > 0)
            MineTweakerAPI.apply(new AddOreAction(oredictKey, processTime));
        else
            MineTweakerAPI.logError("Tried to add empty ore dictionary key to compost table.");
    }

    @ZenMethod
    public static void clear () {
        MineTweakerAPI.apply(new ClearAction());
    }

    private static class AddItemAction implements IUndoableAction {
        private ItemStack material;
        private int processTime;

        public AddItemAction (ItemStack material, int processTime) {
            this.material = material.copy();
            this.processTime = processTime > 0 ? processTime : 150;
        }

        @Override
        public void apply () {
            GardenAPI.instance().registries().compost().registerCompostMaterial(material, new StandardCompostMaterial(processTime, 0.125f));
        }

        @Override
        public boolean canUndo () {
            return true;
        }

        @Override
        public void undo () {
            GardenAPI.instance().registries().compost().removeCompostMaterial(material);
        }

        @Override
        public String describe () {
            return "Adding item '" + material.getDisplayName() + "' to compost table with processing time '" + processTime + "'";
        }

        @Override
        public String describeUndo () {
            return "Removing previously added item '" + material.getDisplayName() + "' from compost table.";
        }

        @Override
        public Object getOverrideKey () {
            return null;
        }
    }

    private static class AddOreAction implements IUndoableAction {
        private String material;
        private int processTime;

        public AddOreAction (String material, int processTime) {
            this.material = material;
            this.processTime = processTime > 0 ? processTime : 150;
        }

        @Override
        public void apply () {
            GardenAPI.instance().registries().compost().registerCompostMaterial(material, new StandardCompostMaterial(processTime, 0.125f));
        }

        @Override
        public boolean canUndo () {
            return true;
        }

        @Override
        public void undo () {
            GardenAPI.instance().registries().compost().removeCompostMaterial(material);
        }

        @Override
        public String describe () {
            return "Adding ore dictionary key '" + material + "' to compost table with processing time '" + processTime + "'";
        }

        @Override
        public String describeUndo () {
            return "Removing previously added ore dictionary key '" + material + "' from compost table.";
        }

        @Override
        public Object getOverrideKey () {
            return null;
        }
    }

    private static class ClearAction implements IUndoableAction {

        @Override
        public void apply () {
            GardenAPI.instance().registries().compost().clear();
        }

        @Override
        public boolean canUndo () {
            return false;
        }

        @Override
        public void undo () { }

        @Override
        public String describe () {
            return "Clearing compost table.";
        }

        @Override
        public String describeUndo () {
            return "";
        }

        @Override
        public Object getOverrideKey () {
            return null;
        }
    }
}

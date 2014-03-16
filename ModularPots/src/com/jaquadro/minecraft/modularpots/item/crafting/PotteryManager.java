package com.jaquadro.minecraft.modularpots.item.crafting;

import com.jaquadro.minecraft.modularpots.ModularPots;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotteryManager
{
    private static final PotteryManager instance = new PotteryManager();

    private List<ItemStack> patternList = new ArrayList<ItemStack>();
    private Map<ItemStack, Integer> targetList = new HashMap<ItemStack, Integer>();

    public static PotteryManager instance () {
        return instance;
    }

    private PotteryManager () {
        for (int i = 1; i <= 5; i++)
            registerPattern(new ItemStack(ModularPots.potteryPattern, 1, i));

        registerTarget(new ItemStack(ModularPots.largePot, 1, 1));
        registerTarget(new ItemStack(ModularPots.largePot));
        for (int i = 0; i < 16; i++)
            registerTarget(new ItemStack(ModularPots.largePotColored, 1, i));
    }

    public void registerPattern (ItemStack itemStack) {
        if (itemStack != null)
            patternList.add(itemStack);
    }

    public void registerTarget (ItemStack itemStack) {
        if (itemStack != null)
            registerTarget(itemStack, 8);
    }

    public void registerTarget (ItemStack itemStack, int patternDataShift) {
        targetList.put(itemStack, patternDataShift);
    }

    public boolean isRegisteredPattern (ItemStack itemStack) {
        if (itemStack == null)
            return false;

        for (ItemStack item : patternList) {
            if (item.getItem() == itemStack.getItem() && item.getItemDamage() == itemStack.getItemDamage())
                return true;
        }

        return false;
    }

    public boolean isRegisteredTarget (ItemStack itemStack) {
        if (itemStack == null)
            return false;

        for (ItemStack item : targetList.keySet()) {
            if (item.getItem() == itemStack.getItem() && item.getItemDamage() == itemStack.getItemDamage())
                return true;
        }

        return false;
    }

    public ItemStack getStampResult (ItemStack pattern, ItemStack target) {
        if (target == null || !isRegisteredPattern(pattern))
            return null;

        for (Map.Entry<ItemStack, Integer> entry : targetList.entrySet()) {
            ItemStack item = entry.getKey();
            if (item.getItem() != target.getItem() || item.getItemDamage() != target.getItemDamage())
                continue;

            ItemStack result = target.copy();
            result.setItemDamage(target.getItemDamage() | (pattern.getItemDamage() << entry.getValue()));

            return result;
        }

        return null;
    }
}

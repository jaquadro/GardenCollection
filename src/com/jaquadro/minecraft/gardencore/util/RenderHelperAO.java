package com.jaquadro.minecraft.gardencore.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class RenderHelperAO
{
    private RenderHelperState state;

    private int aoBrightnessXYNI;
    private int aoBrightnessYZIN;
    private int aoBrightnessYZIP;
    private int aoBrightnessXYPI;
    private int aoBrightnessXYZNIN;
    private int aoBrightnessXYZNIP;
    private int aoBrightnessXYZPIN;
    private int aoBrightnessXYZPIP;

    private int aoBrightnessXYNN;
    private int aoBrightnessYZNN;
    private int aoBrightnessYZNP;
    private int aoBrightnessXYPN;
    private int aoBrightnessXYNP;
    private int aoBrightnessXYPP;
    private int aoBrightnessYZPN;
    private int aoBrightnessYZPP;
    private int aoBrightnessXZNN;
    private int aoBrightnessXZPN;
    private int aoBrightnessXZNP;
    private int aoBrightnessXZPP;
    private int aoBrightnessXYZNNN;
    private int aoBrightnessXYZNNP;
    private int aoBrightnessXYZPNN;
    private int aoBrightnessXYZPNP;
    private int aoBrightnessXYZNPN;
    private int aoBrightnessXYZPPN;
    private int aoBrightnessXYZNPP;
    private int aoBrightnessXYZPPP;

    private int aoBrightnessXZNI;
    private int aoBrightnessYZNI;
    private int aoBrightnessYZPI;
    private int aoBrightnessXZPI;
    private int aoBrightnessXYIN;
    private int aoBrightnessXZIN;
    private int aoBrightnessXZIP;
    private int aoBrightnessXYIP;
    private int aoBrightnessXYZNNI;
    private int aoBrightnessXYZNPI;
    private int aoBrightnessXYZPNI;
    private int aoBrightnessXYZPPI;
    private int aoBrightnessXYZINN;
    private int aoBrightnessXYZINP;
    private int aoBrightnessXYZIPN;
    private int aoBrightnessXYZIPP;

    private float aoLightValueScratchXYNI;
    private float aoLightValueScratchYZIN;
    private float aoLightValueScratchYZIP;
    private float aoLightValueScratchXYPI;
    private float aoLightValueScratchXYZNIN;
    private float aoLightValueScratchXYZNIP;
    private float aoLightValueScratchXYZPIN;
    private float aoLightValueScratchXYZPIP;

    private float aoLightValueScratchXYNN;
    private float aoLightValueScratchYZNN;
    private float aoLightValueScratchYZNP;
    private float aoLightValueScratchXYPN;
    private float aoLightValueScratchXYNP;
    private float aoLightValueScratchXYPP;
    private float aoLightValueScratchYZPN;
    private float aoLightValueScratchYZPP;
    private float aoLightValueScratchXZNN;
    private float aoLightValueScratchXZPN;
    private float aoLightValueScratchXZNP;
    private float aoLightValueScratchXZPP;
    private float aoLightValueScratchXYZNNN;
    private float aoLightValueScratchXYZNNP;
    private float aoLightValueScratchXYZPNN;
    private float aoLightValueScratchXYZPNP;
    private float aoLightValueScratchXYZNPN;
    private float aoLightValueScratchXYZPPN;
    private float aoLightValueScratchXYZNPP;
    private float aoLightValueScratchXYZPPP;

    private float aoLightValueScratchXZNI;
    private float aoLightValueScratchYZNI;
    private float aoLightValueScratchYZPI;
    private float aoLightValueScratchXZPI;
    private float aoLightValueScratchXYIN;
    private float aoLightValueScratchXZIN;
    private float aoLightValueScratchXZIP;
    private float aoLightValueScratchXYIP;
    private float aoLightValueScratchXYZNNI;
    private float aoLightValueScratchXYZNPI;
    private float aoLightValueScratchXYZPNI;
    private float aoLightValueScratchXYZPPI;
    private float aoLightValueScratchXYZINN;
    private float aoLightValueScratchXYZINP;
    private float aoLightValueScratchXYZIPN;
    private float aoLightValueScratchXYZIPP;

    public RenderHelperAO (RenderHelperState state) {
        this.state = state;
    }

    public void setupYNegAOPartial (IBlockAccess blockAccess, Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        int yGrass = (state.renderMinY <= 0) ? y - 1 : y;

        boolean blocksGrassXYPN = !blockAccess.getBlock(x + 1, yGrass, z).getCanBlockGrass();
        boolean blocksGrassXYNN = !blockAccess.getBlock(x - 1, yGrass, z).getCanBlockGrass();
        boolean blocksGrassYZNP = !blockAccess.getBlock(x, yGrass, z + 1).getCanBlockGrass();
        boolean blocksGrassYZNN = !blockAccess.getBlock(x, yGrass, z - 1).getCanBlockGrass();

        if (state.renderMinY > 0)
            setupAOBrightnessYNeg(blockAccess, block, x, y, z, blocksGrassXYPN, blocksGrassXYNN, blocksGrassYZNP, blocksGrassYZNN);

        setupAOBrightnessYPos(blockAccess, block, x, y - 1, z, blocksGrassXYPN, blocksGrassXYNN, blocksGrassYZNP, blocksGrassYZNN);

        float yClamp = MathHelper.clamp_float((float) state.renderMinY, 0, 1);
        mixAOBrightnessLightValueY(yClamp, 1 - yClamp);

        int blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        if (state.renderMinY <= 0.0D || !blockAccess.getBlock(x, y - 1, z).isOpaqueCube())
            blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);

        float aoOpposingBlock = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        float aoXYZNNP = (aoLightValueScratchXYNI + aoLightValueScratchXYZNIP + aoOpposingBlock + aoLightValueScratchYZIP) / 4.0F;
        float aoXYZPNP = (aoOpposingBlock + aoLightValueScratchYZIP + aoLightValueScratchXYPI + aoLightValueScratchXYZPIP) / 4.0F;
        float aoXYZPNN = (aoLightValueScratchYZIN + aoOpposingBlock + aoLightValueScratchXYZPIN + aoLightValueScratchXYPI) / 4.0F;
        float aoXYZNNN = (aoLightValueScratchXYZNIN + aoLightValueScratchXYNI + aoLightValueScratchYZIN + aoOpposingBlock) / 4.0F;

        double minZ = MathHelper.clamp_double(state.renderMinZ, 0, 1);
        double maxZ = MathHelper.clamp_double(state.renderMaxZ, 0, 1);
        double minX = MathHelper.clamp_double(state.renderMinX, 0, 1);
        double maxX = MathHelper.clamp_double(state.renderMaxX, 0, 1);
        
        float aoTR = (float)((double)aoXYZNNP * minX * (1.0D - maxZ) + (double)aoXYZPNP * minX * maxZ + (double)aoXYZPNN * (1.0D - minX) * maxZ + (double)aoXYZNNN * (1.0D - minX) * (1.0D - maxZ));
        float aoTL = (float)((double)aoXYZNNP * minX * (1.0D - minZ) + (double)aoXYZPNP * minX * minZ + (double)aoXYZPNN * (1.0D - minX) * minZ + (double)aoXYZNNN * (1.0D - minX) * (1.0D - minZ));
        float aoBL = (float)((double)aoXYZNNP * maxX * (1.0D - minZ) + (double)aoXYZPNP * maxX * minZ + (double)aoXYZPNN * (1.0D - maxX) * minZ + (double)aoXYZNNN * (1.0D - maxX) * (1.0D - minZ));
        float aoBR = (float)((double)aoXYZNNP * maxX * (1.0D - maxZ) + (double)aoXYZPNP * maxX * maxZ + (double)aoXYZPNN * (1.0D - maxX) * maxZ + (double)aoXYZNNN * (1.0D - maxX) * (1.0D - maxZ));

        int brXYZNNP = getAOBrightness(aoBrightnessXYNI, aoBrightnessXYZNIP, aoBrightnessYZIP, blockBrightness);
        int brXYZPNP = getAOBrightness(aoBrightnessYZIP, aoBrightnessXYPI, aoBrightnessXYZPIP, blockBrightness);
        int brXYZPNN = getAOBrightness(aoBrightnessYZIN, aoBrightnessXYZPIN, aoBrightnessXYPI, blockBrightness);
        int brXYZNNN = getAOBrightness(aoBrightnessXYZNIN, aoBrightnessXYNI, aoBrightnessYZIN, blockBrightness);

        state.brightnessTopRight = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, maxX * (1.0D - maxZ), (1.0D - maxX) * (1.0D - maxZ), (1.0D - maxX) * maxZ, maxX * maxZ);
        state.brightnessTopLeft = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, maxX * (1.0D - minZ), (1.0D - maxX) * (1.0D - minZ), (1.0D - maxX) * minZ, maxX * minZ);
        state.brightnessBottomLeft = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, minX * (1.0D - minZ), (1.0D - minX) * (1.0D - minZ), (1.0D - minX) * minZ, minX * minZ);
        state.brightnessBottomRight = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, minX * (1.0D - maxZ), (1.0D - minX) * (1.0D - maxZ), (1.0D - minX) * maxZ, minX * maxZ);

        state.setColor(r * state.colorMultYNeg, g * state.colorMultYNeg, b * state.colorMultYNeg);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupYPosAOPartial (IBlockAccess blockAccess, Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (state.renderMaxY >= 1.0D)
            ++y;

        aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
        aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
        aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
        aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
        aoBrightnessXYZNPN = aoBrightnessXYNP;
        aoBrightnessXYZPPN = aoBrightnessXYPP;
        aoBrightnessXYZNPP = aoBrightnessXYNP;
        aoBrightnessXYZPPP = aoBrightnessXYPP;

        aoLightValueScratchXYNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
        aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
        aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
        aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;

        boolean blocksGrassXYPP = blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
        boolean blocksGrassXYNP = blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
        boolean blocksGrassYZPP = blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
        boolean blocksGrassYZPN = blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

        if (blocksGrassYZPN || blocksGrassXYNP) {
            aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
        }

        if (blocksGrassYZPN || blocksGrassXYPP) {
            aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
        }

        if (blocksGrassYZPP || blocksGrassXYNP) {
            aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
        }

        if (blocksGrassYZPP || blocksGrassXYPP) {
            aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
        }

        if (state.renderMaxY >= 1.0D)
            --y;

        int blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        if (state.renderMaxY >= 1.0D || !blockAccess.getBlock(x, y + 1, z).isOpaqueCube())
            blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);

        float aoOpposingBlock = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        float aoXYZNPN = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + aoOpposingBlock) / 4.0F;  // TR
        float aoXYZNPP = (aoLightValueScratchYZPP + aoOpposingBlock + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4.0F;  // TL
        float aoXYZPPP = (aoOpposingBlock + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4.0F;  // BL
        float aoXYZPPN = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + aoOpposingBlock + aoLightValueScratchYZPN) / 4.0F;  // BR

        double minZ = MathHelper.clamp_double(state.renderMinZ, 0, 1);
        double maxZ = MathHelper.clamp_double(state.renderMaxZ, 0, 1);
        double minX = MathHelper.clamp_double(state.renderMinX, 0, 1);
        double maxX = MathHelper.clamp_double(state.renderMaxX, 0, 1);
        
        float aoTL = (float)((double)aoXYZPPP * maxX * (1.0D - maxZ) + (double)aoXYZNPP * maxX * maxZ + (double)aoXYZNPN * (1.0D - maxX) * maxZ + (double)aoXYZPPN * (1.0D - maxX) * (1.0D - maxZ));
        float aoBL = (float)((double)aoXYZPPP * maxX * (1.0D - minZ) + (double)aoXYZNPP * maxX * minZ + (double)aoXYZNPN * (1.0D - maxX) * minZ + (double)aoXYZPPN * (1.0D - maxX) * (1.0D - minZ));
        float aoBR = (float)((double)aoXYZPPP * minX * (1.0D - minZ) + (double)aoXYZNPP * minX * minZ + (double)aoXYZNPN * (1.0D - minX) * minZ + (double)aoXYZPPN * (1.0D - minX) * (1.0D - minZ));
        float aoTR = (float)((double)aoXYZPPP * minX * (1.0D - maxZ) + (double)aoXYZNPP * minX * maxZ + (double)aoXYZNPN * (1.0D - minX) * maxZ + (double)aoXYZPPN * (1.0D - minX) * (1.0D - maxZ));

        int brXYZPPN = getAOBrightness(aoBrightnessXYNP, aoBrightnessXYZNPP, aoBrightnessYZPP, blockBrightness);
        int brXYZNPN = getAOBrightness(aoBrightnessYZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, blockBrightness);
        int brXYZNPP = getAOBrightness(aoBrightnessYZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, blockBrightness);
        int brXYZPPP = getAOBrightness(aoBrightnessXYZNPN, aoBrightnessXYNP, aoBrightnessYZPN, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZPPP, brXYZPPN, brXYZNPN, brXYZNPP, maxZ, maxX);
        state.brightnessBottomLeft = mixAOBrightness(brXYZPPP, brXYZPPN, brXYZNPN, brXYZNPP, minZ, maxX);
        state.brightnessBottomRight = mixAOBrightness(brXYZPPP, brXYZPPN, brXYZNPN, brXYZNPP, minZ, minX);
        state.brightnessTopRight = mixAOBrightness(brXYZPPP, brXYZPPN, brXYZNPN, brXYZNPP, maxZ, minX);

        state.setColor(r * state.colorMultYPos, g * state.colorMultYPos, b * state.colorMultYPos);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupZNegAOPartial (IBlockAccess blockAccess, Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        int zGrass = (state.renderMinZ <= 0) ? z - 1 : z;

        boolean blocksGrassXZPN = !blockAccess.getBlock(x + 1, y, zGrass).getCanBlockGrass();
        boolean blocksGrassXZNN = !blockAccess.getBlock(x - 1, y, zGrass).getCanBlockGrass();
        boolean blocksGrassYZPN = !blockAccess.getBlock(x, y + 1, zGrass).getCanBlockGrass();
        boolean blocksGrassYZNN = !blockAccess.getBlock(x, y - 1, zGrass).getCanBlockGrass();

        if (state.renderMinZ > 0)
            setupAOBrightnessZNeg(blockAccess, block, x, y, z, blocksGrassXZPN, blocksGrassXZNN, blocksGrassYZPN, blocksGrassYZNN);

        setupAOBrightnessZPos(blockAccess, block, x, y, z - 1, blocksGrassXZPN, blocksGrassXZNN, blocksGrassYZPN, blocksGrassYZNN);

        float zClamp = MathHelper.clamp_float((float) state.renderMinZ, 0, 1);
        mixAOBrightnessLightValueZ(zClamp, 1 - zClamp);

        int blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        if (state.renderMinZ <= 0.0D || !blockAccess.getBlock(x, y, z - 1).isOpaqueCube())
            blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);

        float aoOpposingBlock = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        float aoXYZNPN = (aoLightValueScratchXZNI + aoLightValueScratchXYZNPI + aoOpposingBlock + aoLightValueScratchYZPI) / 4.0F;
        float aoXYZPPN = (aoOpposingBlock + aoLightValueScratchYZPI + aoLightValueScratchXZPI + aoLightValueScratchXYZPPI) / 4.0F;
        float aoXYZPNN = (aoLightValueScratchYZNI + aoOpposingBlock + aoLightValueScratchXYZPNI + aoLightValueScratchXZPI) / 4.0F;
        float aoXYZNNN = (aoLightValueScratchXYZNNI + aoLightValueScratchXZNI + aoLightValueScratchYZNI + aoOpposingBlock) / 4.0F;

        double minY = MathHelper.clamp_double(state.renderMinY, 0, 1);
        double maxY = MathHelper.clamp_double(state.renderMaxY, 0, 1);
        double minX = MathHelper.clamp_double(state.renderMinX, 0, 1);
        double maxX = MathHelper.clamp_double(state.renderMaxX, 0, 1);
        
        float aoTL = (float)((double)aoXYZNPN * maxY * (1.0D - minX) + (double)aoXYZPPN * maxY * minX + (double)aoXYZPNN * (1.0D - maxY) * minX + (double)aoXYZNNN * (1.0D - maxY) * (1.0D - minX));
        float aoBL = (float)((double)aoXYZNPN * maxY * (1.0D - maxX) + (double)aoXYZPPN * maxY * maxX + (double)aoXYZPNN * (1.0D - maxY) * maxX + (double)aoXYZNNN * (1.0D - maxY) * (1.0D - maxX));
        float aoBR = (float)((double)aoXYZNPN * minY * (1.0D - maxX) + (double)aoXYZPPN * minY * maxX + (double)aoXYZPNN * (1.0D - minY) * maxX + (double)aoXYZNNN * (1.0D - minY) * (1.0D - maxX));
        float aoTR = (float)((double)aoXYZNPN * minY * (1.0D - minX) + (double)aoXYZPPN * minY * minX + (double)aoXYZPNN * (1.0D - minY) * minX + (double)aoXYZNNN * (1.0D - minY) * (1.0D - minX));

        int brXYZNPN = getAOBrightness(aoBrightnessXZNI, aoBrightnessXYZNPI, aoBrightnessYZPI, blockBrightness);
        int brXYZPPN = getAOBrightness(aoBrightnessYZPI, aoBrightnessXZPI, aoBrightnessXYZPPI, blockBrightness);
        int brXYZPNN = getAOBrightness(aoBrightnessYZNI, aoBrightnessXYZPNI, aoBrightnessXZPI, blockBrightness);
        int brXYZNNN = getAOBrightness(aoBrightnessXYZNNI, aoBrightnessXZNI, aoBrightnessYZNI, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, maxY * (1.0D - minX), maxY * minX, (1.0D - maxY) * minX, (1.0D - maxY) * (1.0D - minX));
        state.brightnessBottomLeft = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, maxY * (1.0D - maxX), maxY * maxX, (1.0D - maxY) * maxX, (1.0D - maxY) * (1.0D - maxX));
        state.brightnessBottomRight = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, minY * (1.0D - maxX), minY * maxX, (1.0D - minY) * maxX, (1.0D - minY) * (1.0D - maxX));
        state.brightnessTopRight = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, minY * (1.0D - minX), minY * minX, (1.0D - minY) * minX, (1.0D - minY) * (1.0D - minX));

        state.setColor(r * state.colorMultZNeg, g * state.colorMultZNeg, b * state.colorMultZNeg);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupZPosAOPartial (IBlockAccess blockAccess, Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        int zGrass = (state.renderMaxZ >= 1) ? z + 1 : z;

        boolean blocksGrassXZPP = !blockAccess.getBlock(x + 1, y, zGrass).getCanBlockGrass();
        boolean blocksGrassXZNP = !blockAccess.getBlock(x - 1, y, zGrass).getCanBlockGrass();
        boolean blocksGrassYZPP = !blockAccess.getBlock(x, y + 1, zGrass).getCanBlockGrass();
        boolean blocksGrassYZNP = !blockAccess.getBlock(x, y - 1, zGrass).getCanBlockGrass();

        if (state.renderMaxZ < 1)
            setupAOBrightnessZPos(blockAccess, block, x, y, z, blocksGrassXZPP, blocksGrassXZNP, blocksGrassYZPP, blocksGrassYZNP);

        setupAOBrightnessZNeg(blockAccess, block, x, y, z + 1, blocksGrassXZPP, blocksGrassXZNP, blocksGrassYZPP, blocksGrassYZNP);

        float zClamp = MathHelper.clamp_float((float) state.renderMaxZ, 0, 1);
        mixAOBrightnessLightValueZ(zClamp, 1 - zClamp);

        int blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        if (state.renderMaxZ >= 1.0D || !blockAccess.getBlock(x, y, z + 1).isOpaqueCube())
            blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);

        float aoOpposingBlock = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        float aoXYZNPP = (aoLightValueScratchXZNI + aoLightValueScratchXYZNPI + aoOpposingBlock + aoLightValueScratchYZPI) / 4.0F;
        float aoXYZPPP = (aoOpposingBlock + aoLightValueScratchYZPI + aoLightValueScratchXZPI + aoLightValueScratchXYZPPI) / 4.0F;
        float aoXYZPNP = (aoLightValueScratchYZNI + aoOpposingBlock + aoLightValueScratchXYZPNI + aoLightValueScratchXZPI) / 4.0F;
        float aoXYZNNP = (aoLightValueScratchXYZNNI + aoLightValueScratchXZNI + aoLightValueScratchYZNI + aoOpposingBlock) / 4.0F;

        double minY = MathHelper.clamp_double(state.renderMinY, 0, 1);
        double maxY = MathHelper.clamp_double(state.renderMaxY, 0, 1);
        double minX = MathHelper.clamp_double(state.renderMinX, 0, 1);
        double maxX = MathHelper.clamp_double(state.renderMaxX, 0, 1);

        float aoTL = (float)((double)aoXYZNPP * maxY * (1.0D - minX) + (double)aoXYZPPP * maxY * minX + (double)aoXYZPNP * (1.0D - maxY) * minX + (double)aoXYZNNP * (1.0D - maxY) * (1.0D - minX));
        float aoBL = (float)((double)aoXYZNPP * minY * (1.0D - minX) + (double)aoXYZPPP * minY * minX + (double)aoXYZPNP * (1.0D - minY) * minX + (double)aoXYZNNP * (1.0D - minY) * (1.0D - minX));
        float aoBR = (float)((double)aoXYZNPP * minY * (1.0D - maxX) + (double)aoXYZPPP * minY * maxX + (double)aoXYZPNP * (1.0D - minY) * maxX + (double)aoXYZNNP * (1.0D - minY) * (1.0D - maxX));
        float aoTR = (float)((double)aoXYZNPP * maxY * (1.0D - maxX) + (double)aoXYZPPP * maxY * maxX + (double)aoXYZPNP * (1.0D - maxY) * maxX + (double)aoXYZNNP * (1.0D - maxY) * (1.0D - maxX));

        int brXYZNPP = getAOBrightness(aoBrightnessXZNI, aoBrightnessXYZNPI, aoBrightnessYZPI, blockBrightness);
        int brXYZPPP = getAOBrightness(aoBrightnessYZPI, aoBrightnessXZPI, aoBrightnessXYZPPI, blockBrightness);
        int brXYZPNP = getAOBrightness(aoBrightnessYZNI, aoBrightnessXYZPNI, aoBrightnessXZPI, blockBrightness);
        int brXYZNNP = getAOBrightness(aoBrightnessXYZNNI, aoBrightnessXZNI, aoBrightnessYZNI, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, maxY * (1.0D - minX), (1.0D - maxY) * (1.0D - minX), (1.0D - maxY) * minX, maxY * minX);
        state.brightnessBottomLeft = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, minY * (1.0D - minX), (1.0D - minY) * (1.0D - minX), (1.0D - minY) * minX, minY * minX);
        state.brightnessBottomRight = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, minY * (1.0D - maxX), (1.0D - minY) * (1.0D - maxX), (1.0D - minY) * maxX, minY * maxX);
        state.brightnessTopRight = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, maxY * (1.0D - maxX), (1.0D - maxY) * (1.0D - maxX), (1.0D - maxY) * maxX, maxY * maxX);

        state.setColor(r * state.colorMultZPos, g * state.colorMultZPos, b * state.colorMultZPos);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupXNegAOPartial (IBlockAccess blockAccess, Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        int xGrass = (state.renderMinX <= 0) ? x - 1 : x;

        boolean blocksGrassXYNP = !blockAccess.getBlock(xGrass, y + 1, z).getCanBlockGrass();
        boolean blocksGrassXYNN = !blockAccess.getBlock(xGrass, y - 1, z).getCanBlockGrass();
        boolean blocksGrassXZNN = !blockAccess.getBlock(xGrass, y, z - 1).getCanBlockGrass();
        boolean blocksGrassXZNP = !blockAccess.getBlock(xGrass, y, z + 1).getCanBlockGrass();

        if (state.renderMinX > 0)
            setupAOBrightnessXNeg(blockAccess, block, x, y, z, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        setupAOBrightnessXPos(blockAccess, block, x - 1, y, z, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        float xClamp = MathHelper.clamp_float((float) state.renderMinX, 0, 1);
        mixAOBrightnessLightValueX(xClamp, 1 - xClamp);

        int blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        if (state.renderMinX <= 0.0D || !blockAccess.getBlock(x - 1, y, z).isOpaqueCube())
            blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);

        float aoOpposingBlock = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        float aoXYZNNP = (aoLightValueScratchXYIN + aoLightValueScratchXYZINP + aoOpposingBlock + aoLightValueScratchXZIP) / 4.0F;
        float aoXYZNPP = (aoOpposingBlock + aoLightValueScratchXZIP + aoLightValueScratchXYIP + aoLightValueScratchXYZIPP) / 4.0F;
        float aoXYZNPN = (aoLightValueScratchXZIN + aoOpposingBlock + aoLightValueScratchXYZIPN + aoLightValueScratchXYIP) / 4.0F;
        float aoXYZNNN = (aoLightValueScratchXYZINN + aoLightValueScratchXYIN + aoLightValueScratchXZIN + aoOpposingBlock) / 4.0F;
        
        double minY = MathHelper.clamp_double(state.renderMinY, 0, 1);
        double maxY = MathHelper.clamp_double(state.renderMaxY, 0, 1);
        double minZ = MathHelper.clamp_double(state.renderMinZ, 0, 1);
        double maxZ = MathHelper.clamp_double(state.renderMaxZ, 0, 1);

        float aoTL = (float)((double)aoXYZNPP * maxY * maxZ + (double)aoXYZNPN * maxY * (1.0D - maxZ) + (double)aoXYZNNN * (1.0D - maxY) * (1.0D - maxZ) + (double)aoXYZNNP * (1.0D - maxY) * maxZ);
        float aoBL = (float)((double)aoXYZNPP * maxY * minZ + (double)aoXYZNPN * maxY * (1.0D - minZ) + (double)aoXYZNNN * (1.0D - maxY) * (1.0D - minZ) + (double)aoXYZNNP * (1.0D - maxY) * minZ);
        float aoBR = (float)((double)aoXYZNPP * minY * minZ + (double)aoXYZNPN * minY * (1.0D - minZ) + (double)aoXYZNNN * (1.0D - minY) * (1.0D - minZ) + (double)aoXYZNNP * (1.0D - minY) * minZ);
        float aoTR = (float)((double)aoXYZNPP * minY * maxZ + (double)aoXYZNPN * minY * (1.0D - maxZ) + (double)aoXYZNNN * (1.0D - minY) * (1.0D - maxZ) + (double)aoXYZNNP * (1.0D - minY) * maxZ);

        int brXYZNNP = getAOBrightness(aoBrightnessXYIN, aoBrightnessXYZINP, aoBrightnessXZIP, blockBrightness);
        int brXYZNPP = getAOBrightness(aoBrightnessXZIP, aoBrightnessXYIP, aoBrightnessXYZIPP, blockBrightness);
        int brXYZNPN = getAOBrightness(aoBrightnessXZIN, aoBrightnessXYZIPN, aoBrightnessXYIP, blockBrightness);
        int brXYZNNN = getAOBrightness(aoBrightnessXYZINN, aoBrightnessXYIN, aoBrightnessXZIN, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, maxY * maxZ, maxY * (1.0D - maxZ), (1.0D - maxY) * (1.0D - maxZ), (1.0D - maxY) * maxZ);
        state.brightnessBottomLeft = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, maxY * minZ, maxY * (1.0D - minZ), (1.0D - maxY) * (1.0D - minZ), (1.0D - maxY) * minZ);
        state.brightnessBottomRight = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, minY * minZ, minY * (1.0D - minZ), (1.0D - minY) * (1.0D - minZ), (1.0D - minY) * minZ);
        state.brightnessTopRight = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, minY * maxZ, minY * (1.0D - maxZ), (1.0D - minY) * (1.0D - maxZ), (1.0D - minY) * maxZ);

        state.setColor(r * state.colorMultXNeg, g * state.colorMultXNeg, b * state.colorMultXNeg);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupXPosAOPartial (IBlockAccess blockAccess, Block block, int x, int y, int z, float r, float g, float b) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        int xGrass = (state.renderMaxX >= 1) ? x + 1 : x;

        boolean blocksGrassXYNP = !blockAccess.getBlock(xGrass, y + 1, z).getCanBlockGrass();
        boolean blocksGrassXYNN = !blockAccess.getBlock(xGrass, y - 1, z).getCanBlockGrass();
        boolean blocksGrassXZNN = !blockAccess.getBlock(xGrass, y, z - 1).getCanBlockGrass();
        boolean blocksGrassXZNP = !blockAccess.getBlock(xGrass, y, z + 1).getCanBlockGrass();

        if (state.renderMaxX < 1)
            setupAOBrightnessXPos(blockAccess, block, x, y, z, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        setupAOBrightnessXNeg(blockAccess, block, x + 1, y, z, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        float xClamp = MathHelper.clamp_float((float) state.renderMaxX, 0, 1);
        mixAOBrightnessLightValueX(xClamp, 1 - xClamp);

        int blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        if (state.renderMaxX >= 1.0D || !blockAccess.getBlock(x + 1, y, z).isOpaqueCube())
            blockBrightness = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);

        float aoOpposingBlock = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        float aoXYZPNP = (aoLightValueScratchXYIN + aoLightValueScratchXYZINP + aoOpposingBlock + aoLightValueScratchXZIP) / 4.0F;
        float aoXYZPNN = (aoLightValueScratchXYZINN + aoLightValueScratchXYIN + aoLightValueScratchXZIN + aoOpposingBlock) / 4.0F;
        float aoXYZPPN = (aoLightValueScratchXZIN + aoOpposingBlock + aoLightValueScratchXYZIPN + aoLightValueScratchXYIP) / 4.0F;
        float aoXYZPPP = (aoOpposingBlock + aoLightValueScratchXZIP + aoLightValueScratchXYIP + aoLightValueScratchXYZIPP) / 4.0F;

        double minY = MathHelper.clamp_double(state.renderMinY, 0, 1);
        double maxY = MathHelper.clamp_double(state.renderMaxY, 0, 1);
        double minZ = MathHelper.clamp_double(state.renderMinZ, 0, 1);
        double maxZ = MathHelper.clamp_double(state.renderMaxZ, 0, 1);

        float aoTL = (float)((double)aoXYZPNP * (1.0D - minY) * maxZ + (double)aoXYZPNN * (1.0D - minY) * (1.0D - maxZ) + (double)aoXYZPPN * minY * (1.0D - maxZ) + (double)aoXYZPPP * minY * maxZ);
        float aoBL = (float)((double)aoXYZPNP * (1.0D - minY) * minZ + (double)aoXYZPNN * (1.0D - minY) * (1.0D - minZ) + (double)aoXYZPPN * minY * (1.0D - minZ) + (double)aoXYZPPP * minY * minZ);
        float aoBR = (float)((double)aoXYZPNP * (1.0D - maxY) * minZ + (double)aoXYZPNN * (1.0D - maxY) * (1.0D - minZ) + (double)aoXYZPPN * maxY * (1.0D - minZ) + (double)aoXYZPPP * maxY * minZ);
        float aoTR = (float)((double)aoXYZPNP * (1.0D - maxY) * maxZ + (double)aoXYZPNN * (1.0D - maxY) * (1.0D - maxZ) + (double)aoXYZPPN * maxY * (1.0D - maxZ) + (double)aoXYZPPP * maxY * maxZ);

        int brXYZPNP = getAOBrightness(aoBrightnessXYIN, aoBrightnessXYZINP, aoBrightnessXZIP, blockBrightness);
        int brXYZPNN = getAOBrightness(aoBrightnessXZIP, aoBrightnessXYIP, aoBrightnessXYZIPP, blockBrightness);
        int brXYZPPN = getAOBrightness(aoBrightnessXZIN, aoBrightnessXYZIPN, aoBrightnessXYIP, blockBrightness);
        int brXYZPPP = getAOBrightness(aoBrightnessXYZINN, aoBrightnessXYIN, aoBrightnessXZIN, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - minY) * maxZ, (1.0D - minY) * (1.0D - maxZ), minY * (1.0D - maxZ), minY * maxZ);
        state.brightnessBottomLeft = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - minY) * minZ, (1.0D - minY) * (1.0D - minZ), minY * (1.0D - minZ), minY * minZ);
        state.brightnessBottomRight = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - maxY) * minZ, (1.0D - maxY) * (1.0D - minZ), maxY * (1.0D - minZ), maxY * minZ);
        state.brightnessTopRight = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - maxY) * maxZ, (1.0D - maxY) * (1.0D - maxZ), maxY * (1.0D - maxZ), maxY * maxZ);

        state.setColor(r * state.colorMultXPos, g * state.colorMultXPos, b * state.colorMultXPos);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    private void setupAOBrightnessYNeg (IBlockAccess blockAccess, Block block, int x, int y, int z, boolean bgXP, boolean bgXN, boolean bgZP, boolean bgZN) {
        aoLightValueScratchXYNN = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPN = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
        aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
        aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
        aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;

        aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
        aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
        aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
        aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
        aoBrightnessXYZNNN = aoBrightnessXYNN;
        aoBrightnessXYZNNP = aoBrightnessXYNN;
        aoBrightnessXYZPNN = aoBrightnessXYPN;
        aoBrightnessXYZPNP = aoBrightnessXYPN;

        if (bgXN || bgZN) {
            aoLightValueScratchXYZNNN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
        }

        if (bgXN || bgZP) {
            aoLightValueScratchXYZNNP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
        }

        if (bgXP || bgZN) {
            aoLightValueScratchXYZPNN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
        }

        if (bgXP || bgZP) {
            aoLightValueScratchXYZPNP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
        }
    }

    private void setupAOBrightnessYPos (IBlockAccess blockAccess, Block block, int x, int y, int z, boolean bgXP, boolean bgXN, boolean bgZP, boolean bgZN) {
        aoLightValueScratchXYNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
        aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
        aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
        aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;

        aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
        aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
        aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
        aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
        aoBrightnessXYZNPN = aoBrightnessXYNP;
        aoBrightnessXYZNPP = aoBrightnessXYNP;
        aoBrightnessXYZPPN = aoBrightnessXYPP;
        aoBrightnessXYZPPP = aoBrightnessXYPP;

        if (bgXN || bgZN) {
            aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
        }

        if (bgXN || bgZP) {
            aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
        }

        if (bgXP || bgZN) {
            aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
        }

        if (bgXP || bgZP) {
            aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
        }
    }

    private void setupAOBrightnessZNeg (IBlockAccess blockAccess, Block block, int x, int y, int z, boolean bgXP, boolean bgXN, boolean bgYP, boolean bgYN) {
        aoLightValueScratchXZNN = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPN = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPN = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
        aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;

        aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
        aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
        aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
        aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
        aoBrightnessXYZNNN = aoBrightnessXZNN;
        aoBrightnessXYZNPN = aoBrightnessXZNN;
        aoBrightnessXYZPNN = aoBrightnessXZPN;
        aoBrightnessXYZPPN = aoBrightnessXZPN;

        if (bgXN || bgYN) {
            aoLightValueScratchXYZNNN = blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
        }

        if (bgXN || bgYP) {
            aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
        }

        if (bgXP || bgYN) {
            aoLightValueScratchXYZPNN = blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
        }

        if (bgXP || bgYP) {
            aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
        }
    }

    private void setupAOBrightnessZPos (IBlockAccess blockAccess, Block block, int x, int y, int z, boolean bgXP, boolean bgXN, boolean bgYP, boolean bgYN) {
        aoLightValueScratchXZNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNP = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
        aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
        aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
        aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;

        aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
        aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
        aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
        aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
        aoBrightnessXYZNNP = aoBrightnessXZNP;
        aoBrightnessXYZNPP = aoBrightnessXZNP;
        aoBrightnessXYZPNP = aoBrightnessXZPP;
        aoBrightnessXYZPPP = aoBrightnessXZPP;

        if (bgXN || bgYN) {
            aoLightValueScratchXYZNNP = blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
        }

        if (bgXN || bgYP) {
            aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
        }

        if (bgXP || bgYN) {
            aoLightValueScratchXYZPNP = blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
        }

        if (bgXP || bgYP) {
            aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
        }
    }

    private void setupAOBrightnessXNeg (IBlockAccess blockAccess, Block block, int x, int y, int z, boolean bgYP, boolean bgYN, boolean bgZN, boolean bgZP) {
        aoLightValueScratchXYNN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXZNN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXZNP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXYNP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
        aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;

        aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
        aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
        aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
        aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
        aoBrightnessXYZNNN = aoBrightnessXZNN;
        aoBrightnessXYZNNP = aoBrightnessXZNP;
        aoBrightnessXYZNPN = aoBrightnessXZNN;
        aoBrightnessXYZNPP = aoBrightnessXZNP;

        if (bgZN || bgYN) {
            aoLightValueScratchXYZNNN = blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
        }

        if (bgZP || bgYN) {
            aoLightValueScratchXYZNNP = blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
        }

        if (bgZN || bgYP) {
            aoLightValueScratchXYZNPN = blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
        }

        if (bgZP || bgYP) {
            aoLightValueScratchXYZNPP = blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
        }
    }

    private void setupAOBrightnessXPos (IBlockAccess blockAccess, Block block, int x, int y, int z, boolean bgYP, boolean bgYN, boolean bgZN, boolean bgZP) {
        aoLightValueScratchXYPN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
        aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
        aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
        aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;

        aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
        aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
        aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
        aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
        aoBrightnessXYZPNN = aoBrightnessXZPN;
        aoBrightnessXYZPNP = aoBrightnessXZPP;
        aoBrightnessXYZPPN = aoBrightnessXZPN;
        aoBrightnessXYZPPP = aoBrightnessXZPP;

        if (bgYN || bgZN) {
            aoLightValueScratchXYZPNN = blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
        }

        if (bgYN || bgZP) {
            aoLightValueScratchXYZPNP = blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
        }

        if (bgYP || bgZN) {
            aoLightValueScratchXYZPPN = blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
        }

        if (bgYP || bgZP) {
            aoLightValueScratchXYZPPP = blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
        }
    }

    private void mixAOBrightnessLightValueY (float fMin, float fMax) {
        if (fMin == 1 && fMax == 0) {
            aoLightValueScratchXYNI = aoLightValueScratchXYNN;
            aoLightValueScratchYZIN = aoLightValueScratchYZNN;
            aoLightValueScratchYZIP = aoLightValueScratchYZNP;
            aoLightValueScratchXYPI = aoLightValueScratchXYPN;
            aoLightValueScratchXYZNIN = aoLightValueScratchXYZNNN;
            aoLightValueScratchXYZNIP = aoLightValueScratchXYZNNP;
            aoLightValueScratchXYZPIN = aoLightValueScratchXYZPNN;
            aoLightValueScratchXYZPIP = aoLightValueScratchXYZPNP;

            aoBrightnessXYNI = aoBrightnessXYNN;
            aoBrightnessYZIN = aoBrightnessYZNN;
            aoBrightnessYZIP = aoBrightnessYZNP;
            aoBrightnessXYPI = aoBrightnessXYPN;
            aoBrightnessXYZNIN = aoBrightnessXYZNNN;
            aoBrightnessXYZNIP = aoBrightnessXYZNNP;
            aoBrightnessXYZPIN = aoBrightnessXYZPNN;
            aoBrightnessXYZPIP = aoBrightnessXYZPNP;
        }
        else if (fMin == 0 && fMax == 1) {
            aoLightValueScratchXYNI = aoLightValueScratchXYNP;
            aoLightValueScratchYZIN = aoLightValueScratchYZPN;
            aoLightValueScratchYZIP = aoLightValueScratchYZPP;
            aoLightValueScratchXYPI = aoLightValueScratchXYPP;
            aoLightValueScratchXYZNIN = aoLightValueScratchXYZNPN;
            aoLightValueScratchXYZNIP = aoLightValueScratchXYZNPP;
            aoLightValueScratchXYZPIN = aoLightValueScratchXYZPPN;
            aoLightValueScratchXYZPIP = aoLightValueScratchXYZPPP;

            aoBrightnessXYNI = aoBrightnessXYNP;
            aoBrightnessYZIN = aoBrightnessYZPN;
            aoBrightnessYZIP = aoBrightnessYZPP;
            aoBrightnessXYPI = aoBrightnessXYPP;
            aoBrightnessXYZNIN = aoBrightnessXYZNPN;
            aoBrightnessXYZNIP = aoBrightnessXYZNPP;
            aoBrightnessXYZPIN = aoBrightnessXYZPPN;
            aoBrightnessXYZPIP = aoBrightnessXYZPPP;
        }
        else {
            aoLightValueScratchXYNI = aoLightValueScratchXYNN * fMin + aoLightValueScratchXYNP * fMax;
            aoLightValueScratchYZIN = aoLightValueScratchYZNN * fMin + aoLightValueScratchYZPN * fMax;
            aoLightValueScratchYZIP = aoLightValueScratchYZNP * fMin + aoLightValueScratchYZPP * fMax;
            aoLightValueScratchXYPI = aoLightValueScratchXYPN * fMin + aoLightValueScratchXYPP * fMax;
            aoLightValueScratchXYZNIN = aoLightValueScratchXYZNNN * fMin + aoLightValueScratchXYZNPN * fMax;
            aoLightValueScratchXYZNIP = aoLightValueScratchXYZNNP * fMin + aoLightValueScratchXYZNPP * fMax;
            aoLightValueScratchXYZPIN = aoLightValueScratchXYZPNN * fMin + aoLightValueScratchXYZPPN * fMax;
            aoLightValueScratchXYZPIP = aoLightValueScratchXYZPNP * fMin + aoLightValueScratchXYZPPP * fMax;

            aoBrightnessXYNI = mixAOBrightness(aoBrightnessXYNN, aoBrightnessXYNP, fMin, fMax);
            aoBrightnessYZIN = mixAOBrightness(aoBrightnessYZNN, aoBrightnessYZPN, fMin, fMax);
            aoBrightnessYZIP = mixAOBrightness(aoBrightnessYZNP, aoBrightnessYZPP, fMin, fMax);
            aoBrightnessXYPI = mixAOBrightness(aoBrightnessXYPN, aoBrightnessXYPP, fMin, fMax);
            aoBrightnessXYZNIN = mixAOBrightness(aoBrightnessXYZNNN, aoBrightnessXYZNPN, fMin, fMax);
            aoBrightnessXYZNIP = mixAOBrightness(aoBrightnessXYZNNP, aoBrightnessXYZNPP, fMin, fMax);
            aoBrightnessXYZPIN = mixAOBrightness(aoBrightnessXYZPNN, aoBrightnessXYZPPN, fMin, fMax);
            aoBrightnessXYZPIP = mixAOBrightness(aoBrightnessXYZPNP, aoBrightnessXYZPPP, fMin, fMax);
        }
    }

    private void mixAOBrightnessLightValueZ (float fMin, float fMax) {
        if (fMin == 1 && fMax == 0) {
            aoLightValueScratchXZNI = aoLightValueScratchXZNN;
            aoLightValueScratchYZNI = aoLightValueScratchYZNN;
            aoLightValueScratchYZPI = aoLightValueScratchYZPN;
            aoLightValueScratchXZPI = aoLightValueScratchXZPN;
            aoLightValueScratchXYZNNI = aoLightValueScratchXYZNNN;
            aoLightValueScratchXYZNPI = aoLightValueScratchXYZNPN;
            aoLightValueScratchXYZPNI = aoLightValueScratchXYZPNN;
            aoLightValueScratchXYZPPI = aoLightValueScratchXYZPPN;

            aoBrightnessXZNI = aoBrightnessXZNN;
            aoBrightnessYZNI = aoBrightnessYZNN;
            aoBrightnessYZPI = aoBrightnessYZPN;
            aoBrightnessXZPI = aoBrightnessXZPN;
            aoBrightnessXYZNNI = aoBrightnessXYZNNN;
            aoBrightnessXYZNPI = aoBrightnessXYZNPN;
            aoBrightnessXYZPNI = aoBrightnessXYZPNN;
            aoBrightnessXYZPPI = aoBrightnessXYZPPN;
        }
        else if (fMin == 0 && fMax == 1) {
            aoLightValueScratchXZNI = aoLightValueScratchXZNP;
            aoLightValueScratchYZNI = aoLightValueScratchYZNP;
            aoLightValueScratchYZPI = aoLightValueScratchYZPP;
            aoLightValueScratchXZPI = aoLightValueScratchXZPP;
            aoLightValueScratchXYZNNI = aoLightValueScratchXYZNNP;
            aoLightValueScratchXYZNPI = aoLightValueScratchXYZNPP;
            aoLightValueScratchXYZPNI = aoLightValueScratchXYZPNP;
            aoLightValueScratchXYZPPI = aoLightValueScratchXYZPPP;

            aoBrightnessXZNI = aoBrightnessXZNP;
            aoBrightnessYZNI = aoBrightnessYZNP;
            aoBrightnessYZPI = aoBrightnessYZPP;
            aoBrightnessXZPI = aoBrightnessXZPP;
            aoBrightnessXYZNNI = aoBrightnessXYZNNP;
            aoBrightnessXYZNPI = aoBrightnessXYZNPP;
            aoBrightnessXYZPNI = aoBrightnessXYZPNP;
            aoBrightnessXYZPPI = aoBrightnessXYZPPP;
        }
        else {
            aoLightValueScratchXZNI = aoLightValueScratchXZNN * fMin + aoLightValueScratchXZNP * fMax;
            aoLightValueScratchYZNI = aoLightValueScratchYZNN * fMin + aoLightValueScratchYZNP * fMax;
            aoLightValueScratchYZPI = aoLightValueScratchYZPN * fMin + aoLightValueScratchYZPP * fMax;
            aoLightValueScratchXZPI = aoLightValueScratchXZPN * fMin + aoLightValueScratchXZPP * fMax;
            aoLightValueScratchXYZNNI = aoLightValueScratchXYZNNN * fMin + aoLightValueScratchXYZNNP * fMax;
            aoLightValueScratchXYZNPI = aoLightValueScratchXYZNPN * fMin + aoLightValueScratchXYZNPP * fMax;
            aoLightValueScratchXYZPNI = aoLightValueScratchXYZPNN * fMin + aoLightValueScratchXYZPNP * fMax;
            aoLightValueScratchXYZPPI = aoLightValueScratchXYZPPN * fMin + aoLightValueScratchXYZPPP * fMax;

            aoBrightnessXZNI = mixAOBrightness(aoBrightnessXZNN, aoBrightnessXZNP, fMin, fMax);
            aoBrightnessYZNI = mixAOBrightness(aoBrightnessYZNN, aoBrightnessYZNP, fMin, fMax);
            aoBrightnessYZPI = mixAOBrightness(aoBrightnessYZPN, aoBrightnessYZPP, fMin, fMax);
            aoBrightnessXZPI = mixAOBrightness(aoBrightnessXZPN, aoBrightnessXZPP, fMin, fMax);
            aoBrightnessXYZNNI = mixAOBrightness(aoBrightnessXYZNNN, aoBrightnessXYZNNP, fMin, fMax);
            aoBrightnessXYZNPI = mixAOBrightness(aoBrightnessXYZNPN, aoBrightnessXYZNPP, fMin, fMax);
            aoBrightnessXYZPNI = mixAOBrightness(aoBrightnessXYZPNN, aoBrightnessXYZPNP, fMin, fMax);
            aoBrightnessXYZPPI = mixAOBrightness(aoBrightnessXYZPPN, aoBrightnessXYZPPP, fMin, fMax);
        }
    }

    private void mixAOBrightnessLightValueX (float fMin, float fMax) {
        if (fMin == 1 && fMax == 0) {
            aoLightValueScratchXYIN = aoLightValueScratchXYNN;
            aoLightValueScratchXZIN = aoLightValueScratchXZNN;
            aoLightValueScratchXZIP = aoLightValueScratchXZNP;
            aoLightValueScratchXYIP = aoLightValueScratchXYNP;
            aoLightValueScratchXYZINN = aoLightValueScratchXYZNNN;
            aoLightValueScratchXYZINP = aoLightValueScratchXYZNNP;
            aoLightValueScratchXYZIPN = aoLightValueScratchXYZNPN;
            aoLightValueScratchXYZIPP = aoLightValueScratchXYZNPP;

            aoBrightnessXYIN = aoBrightnessXYNN;
            aoBrightnessXZIN = aoBrightnessXZNN;
            aoBrightnessXZIP = aoBrightnessXZNP;
            aoBrightnessXYIP = aoBrightnessXYNP;
            aoBrightnessXYZINN = aoBrightnessXYZNNN;
            aoBrightnessXYZINP = aoBrightnessXYZNNP;
            aoBrightnessXYZIPN = aoBrightnessXYZNPN;
            aoBrightnessXYZIPP = aoBrightnessXYZNPP;
        }
        else if (fMin == 0 && fMax == 1) {
            aoLightValueScratchXYIN = aoLightValueScratchXYPN;
            aoLightValueScratchXZIN = aoLightValueScratchXZPN;
            aoLightValueScratchXZIP = aoLightValueScratchXZPP;
            aoLightValueScratchXYIP = aoLightValueScratchXYPP;
            aoLightValueScratchXYZINN = aoLightValueScratchXYZPNN;
            aoLightValueScratchXYZINP = aoLightValueScratchXYZPNP;
            aoLightValueScratchXYZIPN = aoLightValueScratchXYZPPN;
            aoLightValueScratchXYZIPP = aoLightValueScratchXYZPPP;

            aoBrightnessXYIN = aoBrightnessXYPN;
            aoBrightnessXZIN = aoBrightnessXZPN;
            aoBrightnessXZIP = aoBrightnessXZPP;
            aoBrightnessXYIP = aoBrightnessXYPP;
            aoBrightnessXYZINN = aoBrightnessXYZPNN;
            aoBrightnessXYZINP = aoBrightnessXYZPNP;
            aoBrightnessXYZIPN = aoBrightnessXYZPPN;
            aoBrightnessXYZIPP = aoBrightnessXYZPPP;
        }
        else {
            aoLightValueScratchXYIN = aoLightValueScratchXYNN * fMin + aoLightValueScratchXYPN * fMax;
            aoLightValueScratchXZIN = aoLightValueScratchXZNN * fMin + aoLightValueScratchXZPN * fMax;
            aoLightValueScratchXZIP = aoLightValueScratchXZNP * fMin + aoLightValueScratchXZPP * fMax;
            aoLightValueScratchXYIP = aoLightValueScratchXYNP * fMin + aoLightValueScratchXYPP * fMax;
            aoLightValueScratchXYZINN = aoLightValueScratchXYZNNN * fMin + aoLightValueScratchXYZPNN * fMax;
            aoLightValueScratchXYZINP = aoLightValueScratchXYZNNP * fMin + aoLightValueScratchXYZPNP * fMax;
            aoLightValueScratchXYZIPN = aoLightValueScratchXYZNPN * fMin + aoLightValueScratchXYZPPN * fMax;
            aoLightValueScratchXYZIPP = aoLightValueScratchXYZNPP * fMin + aoLightValueScratchXYZPPP * fMax;

            aoBrightnessXYIN = mixAOBrightness(aoBrightnessXYNN, aoBrightnessXYPN, fMin, fMax);
            aoBrightnessXZIN = mixAOBrightness(aoBrightnessXZNN, aoBrightnessXZPN, fMin, fMax);
            aoBrightnessXZIP = mixAOBrightness(aoBrightnessXZNP, aoBrightnessXZPP, fMin, fMax);
            aoBrightnessXYIP = mixAOBrightness(aoBrightnessXYNP, aoBrightnessXYPP, fMin, fMax);
            aoBrightnessXYZINN = mixAOBrightness(aoBrightnessXYZNNN, aoBrightnessXYZPNN, fMin, fMax);
            aoBrightnessXYZINP = mixAOBrightness(aoBrightnessXYZNNP, aoBrightnessXYZPNP, fMin, fMax);
            aoBrightnessXYZIPN = mixAOBrightness(aoBrightnessXYZNPN, aoBrightnessXYZPPN, fMin, fMax);
            aoBrightnessXYZIPP = mixAOBrightness(aoBrightnessXYZNPP, aoBrightnessXYZPPP, fMin, fMax);
        }
    }

    public static int getAOBrightness (int com1, int com2, int com3, int base) {
        if (com1 == 0)
            com1 = base;
        if (com2 == 0)
            com2 = base;
        if (com3 == 0)
            com3 = base;

        return com1 + com2 + com3 + base >> 2 & 16711935;
    }

    public static int mixAOBrightness (int part1, int part2, int part3, int part4, double weight1, double weight2, double weight3, double weight4) {
        int brightSky = (int)((part1 >> 16 & 255) * weight1 + (part2 >> 16 & 255) * weight2 + (part3 >> 16 & 255) * weight3 + (part4 >> 16 & 255) * weight4) & 255;
        int brightBlk = (int)((part1 & 255) * weight1 + (part2 & 255) * weight2 + (part3 & 255) * weight3 + (part4 & 255) * weight4) & 255;

        return brightSky << 16 | brightBlk;
    }

    public static int mixAOBrightness (int brightTL, int brightBL, int brightBR, int brightTR, double lerpTB, double lerpLR) {
        double brightSkyL = (brightTL >> 16 & 255) * (1 - lerpTB) + (brightBL >> 16 & 255) * lerpTB;
        double brightSkyR = (brightTR >> 16 & 255) * (1 - lerpTB) + (brightBR >> 16 & 255) * lerpTB;
        int brightSky = (int)(brightSkyL * (1 - lerpLR) + brightSkyR * lerpLR) & 255;

        double brightBlkL = (brightTL & 255) * (1 - lerpTB) + (brightBL & 255) * lerpTB;
        double brightBlkR = (brightTR & 255) * (1 - lerpTB) + (brightBR & 255) * lerpTB;
        int brightBlk = (int)(brightBlkL * (1 - lerpLR) + brightBlkR * lerpLR) & 255;

        return brightSky << 16 | brightBlk;
    }

    public static int mixAOBrightness (int brightMin, int brightMax, float fMin, float fMax) {
        if (brightMin == 0)
            return 0;
        if (brightMax == 0)
            return 0;

        float brightSky = (brightMin >> 16 & 255) * fMin + (brightMax >> 16 & 255) * fMax;
        float brightBlk = (brightMin & 255) * fMin + (brightMax & 255) * fMax;

        return ((int)brightSky & 255) << 16 | ((int)brightBlk & 255);
    }
}

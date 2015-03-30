package com.jaquadro.minecraft.gardencore.block.support;

import com.jaquadro.minecraft.gardencore.api.block.IGardenBlock;
import com.jaquadro.minecraft.gardencore.api.block.garden.IConnectionProfile;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

public class Slot14ProfileBounded extends Slot14Profile
{
    private IGardenBlock garden;

    private AxisAlignedBB[] bound = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1)
    };
    private AxisAlignedBB[] boundS = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 0.0625, 1 - 0.3125),
        AxisAlignedBB.getBoundingBox(0, 0.0625, 0, 1, 0.125, 1 - 0.25),
        AxisAlignedBB.getBoundingBox(0, 0.125, 0, 1, 1, 1 - 0.1875)
    };
    private AxisAlignedBB[] boundN = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0, 0, 0.3125, 1, 0.0625, 1),
        AxisAlignedBB.getBoundingBox(0, 0.0625, 0.25, 1, 0.125, 1),
        AxisAlignedBB.getBoundingBox(0, 0.125, 0.1875, 1, 1, 1)
    };
    private AxisAlignedBB[] boundE = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0, 0, 0, 1 - 0.3125, 0.0625, 1),
        AxisAlignedBB.getBoundingBox(0, 0.0625, 0, 1 - 0.25, 0.125, 1),
        AxisAlignedBB.getBoundingBox(0, 0.125, 0, 1 - 0.1875, 1, 1)
    };
    private AxisAlignedBB[] boundW = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0.3125, 0, 0, 1, 0.0625, 1),
        AxisAlignedBB.getBoundingBox(0.25, 0.0625, 0, 1, 0.125, 1),
        AxisAlignedBB.getBoundingBox(0.1875, 0.125, 0, 1, 1, 1)
    };
    private AxisAlignedBB[] boundSW = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0.3125, 0, 0, 1, 0.0625, 1 - 0.3125),
        AxisAlignedBB.getBoundingBox(0.25, 0.0625, 0, 1, 0.125, 1 - 0.25),
        AxisAlignedBB.getBoundingBox(0.1875, 0.125, 0, 1, 1, 1 - 0.1875)
    };
    private AxisAlignedBB[] boundSE = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0, 0, 0, 1 - 0.3125, 0.0625, 1 - 0.3125),
        AxisAlignedBB.getBoundingBox(0, 0.0625, 0, 1 - 0.25, 0.125, 1 - 0.25),
        AxisAlignedBB.getBoundingBox(0, 0.125, 0, 1 - 0.1875, 1, 1 - 0.1875)
    };
    private AxisAlignedBB[] boundNW = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0.3125, 0, 0.3125, 1, 0.0625, 1),
        AxisAlignedBB.getBoundingBox(0.25, 0.0625, 0.25, 1, 0.125, 1),
        AxisAlignedBB.getBoundingBox(0.1875, 0.125, 0.1875, 1, 1, 1)
    };
    private AxisAlignedBB[] boundNE = new AxisAlignedBB[] {
        AxisAlignedBB.getBoundingBox(0, 0, 0.3125, 1 - 0.1875, 0.0625, 1),
        AxisAlignedBB.getBoundingBox(0, 0.0625, 0.25, 1 - 0.25, 0.125, 1),
        AxisAlignedBB.getBoundingBox(0, 0.125, 0.1875, 1 - 0.1875, 1, 1)
    };

    public Slot14ProfileBounded (IGardenBlock garden, BasicSlotProfile.Slot[] slots) {
        super(slots);
        this.garden = garden;
    }

    @Override
    public AxisAlignedBB[] getClippingBounds (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        IConnectionProfile connection = garden.getConnectionProfile();
        AxisAlignedBB[] activeBound = bound;

        switch (slot) {
            case SLOT_NW:
                activeBound = boundNW;
                if (connection.isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z - 1)) {
                    boolean connectedN = connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z - 1);
                    boolean connectedW = connection.isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z);
                    if (connectedN && connectedW)
                        activeBound = bound;
                    else if (connectedN)
                        activeBound = boundN;
                    else if (connectedW)
                        activeBound = boundW;
                }
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z - 1))
                    activeBound = boundW;
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z))
                    activeBound = boundN;
                break;

            case SLOT_NE:
                activeBound = boundNE;
                if (connection.isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z - 1)) {
                    boolean connectedN = connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z - 1);
                    boolean connectedE = connection.isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z);
                    if (connectedN && connectedE)
                        activeBound = bound;
                    else if (connectedN)
                        activeBound = boundN;
                    else if (connectedE)
                        activeBound = boundE;
                }
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z - 1))
                    activeBound = boundE;
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z))
                    activeBound = boundN;
                break;

            case SLOT_SW:
                activeBound = boundSW;
                if (connection.isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z + 1)) {
                    boolean connectedS = connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z + 1);
                    boolean connectedW = connection.isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z);
                    if (connectedS && connectedW)
                        activeBound = bound;
                    else if (connectedS)
                        activeBound = boundS;
                    else if (connectedW)
                        activeBound = boundW;
                }
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z + 1))
                    activeBound = boundW;
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z))
                    activeBound = boundS;
                break;

            case SLOT_SE:
                activeBound = boundSE;
                if (connection.isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z + 1)) {
                    boolean connectedS = connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z + 1);
                    boolean connectedE = connection.isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z);
                    if (connectedS && connectedE)
                        activeBound = bound;
                    else if (connectedS)
                        activeBound = boundS;
                    else if (connectedE)
                        activeBound = boundE;
                }
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x, y, z + 1))
                    activeBound = boundE;
                else if (connection.isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z))
                    activeBound = boundS;
                break;

            default:
                break;
        }

        return activeBound;
    }
}

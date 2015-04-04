package com.jaquadro.minecraft.gardenapi.api;

import com.jaquadro.minecraft.gardenapi.api.IGardenAPI;

/**
 * Entry point for the public API.
 */
public class GardenAPI
{
    private static IGardenAPI instance;

    /**
     * API entry point.
     *
     * @return The {@link IGardenAPI} instance or null if the API or Garden Core is unavailable.
     */
    public static IGardenAPI instance () {
        if (instance == null) {
            try {
                Class classAPI = Class.forName("com.jaquadro.minecraft.gardenapi.internal.Api");
                instance = (IGardenAPI) classAPI.getField("instance").get(null);
            }
            catch (Throwable t) {
                return null;
            }
        }

        return instance;
    }
}

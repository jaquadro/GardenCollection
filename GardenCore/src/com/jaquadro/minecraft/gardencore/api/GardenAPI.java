package com.jaquadro.minecraft.gardencore.api;

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
                Class classAPI = Class.forName("com.jaquadro.minecraft.gardencore.core.api.Api");
                instance = (IGardenAPI) classAPI.getField("instance").get(null);
            }
            catch (Throwable t) {
                return null;
            }
        }

        return instance;
    }
}

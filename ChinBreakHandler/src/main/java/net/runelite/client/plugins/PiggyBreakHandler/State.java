package net.runelite.client.plugins.PiggyBreakHandler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor
public enum State
{
    NULL,

    LOGIN_SCREEN,
    INVENTORY,
    RESUME,

    LOGOUT,
    LOGOUT_TAB,
    LOGOUT_BUTTON,
    LOGOUT_WAIT
}

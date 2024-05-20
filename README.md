# PiggyPlugins

This fork is a modification of [`0Hutch/PiggyPlugins`](https://github.com/0Hutch/PiggyPlugins). 
It has modifications included that prevent interference or conflicts when running alongside 
[`KALE1111/RuneBotEVP`](https://github.com/KALE1111/RuneBotEVP) and [`KALE1111/rblaunch`](https://github.com/KALE1111/rblaunch).

## About
This plugin aims to prevent RuneLite refusing to load plugins with the same names from `PiggyPlugins`, `RuneBot`, and `jampack`. 
It also has some bug fixes and minor modifications to the `PiggyPlugins` repository; these modifications are submitted to the 
base repository, `0Hutch/PiggyPlugins` for review, and may be merged into the base repository if accepted. Changes from the 
base repository are automatically pulled using [GitHub Actions](https://github.com/jarromie/PiggyPlugins/actions). All credit for these plugins go to their respective authors; 
a majority are (to my knowledge) created by [`0Hutch`](https://github.com/0Hutch), [`polymorphicJ`](https://github.com/polymorphicJ), 
and [`Ethan-Vann`](https://github.com/Ethan-Vann). For a full list of contributors to the base repository (`0Hutch/PiggyPlugins`) 
and this fork, check out the [contributors page](https://github.com/jarromie/PiggyPlugins/graphs/contributors) for this repository.

## Changes
* renamed class `EthanApiPlugin` -> `PiggyApiPlugin`
* renamed class `PacketUtilsPlugin` -> `PacketApiPlugin`
* renamed class `BetterProfilesPlugin` -> `PiggyProfilesPlugin`
* renamed class `ChinBreakHandlerPlugin` -> `PiggyBreakHandlerPlugin`
* changed plugin display name `Packet Utils` -> `[PP] [API] PacketApi`
* changed plugin display name `EthanApiPlugin` -> `[PP] [API] EthanApi`
* changed plugin display name `PathingTesting` -> `[PP] [API] PathingApi`
* changed plugin display name `[PP] Chin Break Handler` -> `[PP] Break Handler`
* added build settings and config for `AoeWarn` - area projectile warnings
* added build settings and config for `AutoCombatv2` - new and improved combat plugin
* added build settings and config for `ScurriusSpines` - redeems Scurrius spines for XP
* added the `[PP]` (in color) prefix to the names of plugins that were missing it
* changed RuneLite version from `1.10.22` to `latest.release`
* changed PiggyPlugins version from `1.1.8` to `1.1.8-patch`

For a full list of changes made to this fork, check out the [commit history](https://github.com/jarromie/PiggyPlugins/commits/master/) or [compare branches](https://github.com/0Hutch/PiggyPlugins/compare/master...jarromie:PiggyPlugins:master).

## Support
If you need help, feel free to add me on Discord (`jarromie`). When `jampack` is ready, there will be a dedicated Discord and Matrix 
server for both the `jampack` plugin pack, and this fork of `PiggyPlugins` as well.
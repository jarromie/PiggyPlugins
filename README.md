# PiggyPlugins

This fork is a modification of [`0Hutch/PiggyPlugins`](https://github.com/0Hutch/PiggyPlugins). 
It has modifications included that prevent interference or conflicts when running alongside 
[`KALE1111/RuneBotEVP`](https://github.com/KALE1111/RuneBotEVP) and [`KALE1111/rblaunch`](https://github.com/KALE1111/rblaunch).

## About
This plugin aims to prevent RuneLite refusing to load plugins with the same names from `PiggyPlugins`, `RuneBot`, and `jampack`. 
It also has some bug fixes and minor modifications to the `PiggyPlugins` repository; these modifications are submitted to the 
base repository, `0Hutch/PiggyPlugins` for review, and may be merged into the base repository if accepted. Changes from the 
base repository are automatically pulled using [GitHub Actions](https://github.com/jarromie/PiggyPlugins/actions).

## Changes
* renamed `EthanApiPlugin` -> `PiggyApiPlugin`
* renamed `PacketUtilsPlugin` -> `PacketApiPlugin`
* renamed `BetterProfilesPlugin` -> `PiggyProfilesPlugin`
* renamed `ChinBreakHandlerPlugin` -> `PiggyBreakHandlerPlugin`
* added build settings and  config for `AutoCombatv2` - info [here](https://github.com/0Hutch/PiggyPlugins/pull/56)
* changed RuneLite version from `1.10.22` to `latest.release`
* changed PiggyPlugins version from `1.1.8` to `1.1.8-patch`

For a full list of changes made to this fork, check out the [commit history](https://github.com/jarromie/PiggyPlugins/commits/master/) or [compare branches](https://github.com/0Hutch/PiggyPlugins/compare/master...jarromie:PiggyPlugins:master).

## Support
If you need help, feel free to add me on Discord (`jarromie`).
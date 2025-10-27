# CustomPrefix

A lightweight Paper plugin that applies LuckPerms MiniMessage-based `prefix`, `suffix`, and a customizable `namestyle` to player nameplates and chat. Written in Kotlin, built with Gradle.

## Requirements

- PaperMC 1.21.10 used but should work on any 1.21+ version
- Java 21
- LuckPerms 5.4+ (Bukkit)

## Installation

1. Build the plugin or download the release.
2. Copy `CustomPrefix-1.0.0.jar` to your server `plugins` directory.
3. Start or restart the server.

## How it works

- Reads LuckPerms user meta:
    - `prefix`: MiniMessage applied before the player name.
    - `suffix`: MiniMessage applied after the player name.
    - `namestyle`: MiniMessage template for the player name itself. Must include the placeholder ``<playername>`` where the raw name should appear.
- Values are parsed with Adventure MiniMessage and applied to player nameplates/chat. Changes to LuckPerms meta are picked up live.
- Use [webui by advntr](https://webui.advntr.dev/) to edit and preview MiniMessage styles easily.
- [Adventure MiniMessage documentation](https://docs.papermc.io/adventure/minimessage/) for reference on supported tags.

## Commands (LuckPerms)

All values use MiniMessage as the parser.

- Set name style (must include ``<playername>``):
    - `/luckperms user USERNAME meta set namestyle "<playername>"`
- Set prefix:
    - `/luckperms user USERNAME meta setprefix "MINIMESSAGE"`
- Set suffix:
    - `/luckperms user USERNAME meta setsuffix "MINIMESSAGE"`

## Troubleshooting

- Verify LuckPerms is installed and enabled.
- Ensure the user has the meta keys set (`prefix`, `suffix`, `namestyle`).
- If the change does not appear immediately, try relogging or `/lp sync`. The plugin listens for LuckPerms meta changes and updates live.

## Build from source

- Requires Java 21.
- Build jar:
    - `./gradlew build`
- Run a test server:
    - `./gradlew runServer` (uses Paper `1.21.10` by default)

Artifacts:
- `build/libs/CustomPrefix-1.0.0-all.jar` (shaded, recommended)
- `build/libs/CustomPrefix-1.0.0.jar` (non-shaded)

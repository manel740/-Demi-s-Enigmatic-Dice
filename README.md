# 🎲 Enigmatic Dice

A Minecraft mod that adds enigmatic dice featuring epic random events, powerful artifacts, unique weapons, and deep custom mechanics.

---

## 📋 Information

| Detail | Specification |
| :--- | :--- |
| **Minecraft Version** | 1.21.1 |
| **Mod Loader** | NeoForge 21.1.73+ |
| **Mod Version** | 1.3.1-1.21.1 |
| **Java Version** | Java 21 (Required) |

---

## ✨ Features

### 🎲 Enigmatic Dice
- **Unique Random Events:** Roll the die to trigger one of over 30+ unique events.
- **Diverse Outcomes:** Includes summons, powerful buffs, severe debuffs, teleportation, and environmental transformations.
- **Mod Integration:** Events seamlessly integrate with popular mods like *Alex's Mobs*, *Alex's Caves*, *Simply Swords*, *Born in Chaos*, and more.
- **Configurable Probabilities:** Fully customizable event chances and loot tables via configuration files.

### ⚔️ Powerful Artifacts & Weapons
- **Soul Eater:** A sword that absorbs souls, trading health for massive damage and temporary max health boosts.
- **Unequal Exchange:** A high-risk, high-reward weapon that trades player stats for devastating enemy health reduction.
- **Crucible of Rile:** A legendary weapon featuring taunts, counterattacks, execute thresholds, and permanent armor scaling.
- **Permafrost:** A blade that freezes enemies, stacking movement/flying speed debuffs and freezing nearby water.
- **Gravity Core:** A curios item that allows epic double-jumps and devastating ground-slams with adjustable gravity.
- **Moon & Moon Shard:** Manipulate lunar gravity and grant fall damage immunity.
- **Giants Ring:** Increases the wearer's size, health, damage, and allows trampling smaller enemies while sprinting.
- **Ring of Agility:** Grants a dynamic dodge chance based on the player's movement speed.
- **Divine Shield:** Blocks incoming damage on a configurable cooldown.
- **Moai Charm:** Grants complete knockback immunity.
- **Four-Leaf Clover:** passively increases luck for each clover in the inventory.
- **Unfinished Weapon:** A mysterious, unbreakable item with hidden potential.

### ⚙️ Advanced Mechanics
- **Custom Attribute System:** Unique, balanced stat modifications for every item.
- **Persistent Player Data:** Utilizes NeoForge Data Attachments for reliable, cross-session stat tracking.
- **Seamless Curios Integration:** All artifacts are fully compatible with the Curios API slot system.

---

## 📦 Requirements

- **Minecraft:** 1.21.1
- **Mod Loader:** NeoForge (21.1.73 or higher)
- **Java:** Java 21 (Eclipse Adoptium, Oracle, or Microsoft builds recommended)
- **Dependencies:** [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) (Required)

---

## 🚀 Installation

1. Install **Java 21** on your system.
2. Install the **NeoForge** installer for Minecraft 1.21.1.
3. Download the latest **Curios API** version for NeoForge 1.21.1.
4. Download the `enigmatic-dice-1.3.1-1.21.1.jar` file.
5. Place both the Curios API and Enigmatic Dice `.jar` files into your `.minecraft/mods` folder.
6. Launch the game and enjoy!

---

## 🎮 Usage

- **Enigmatic Dice:** Can be found in creative mode tab, or obtained naturally through mob drops, block breaks, and chest loot (configurable). Right-click to roll and trigger an event.
- **Artifacts & Weapons:** Equip rings, charms, and belts via the Curios inventory screen. Weapons function like standard tools but with their unique, config-driven abilities.

---

## ⚙️ Configuration

The mod features a robust, auto-updating configuration system:
- **`enigmaticdice-common.toml`**: Controls drop chances, time intervals, and numerical values for all item stats (damage, speed, cooldowns, etc.).
- **`enigmatic_dice.json`**: A dynamic JSON file that allows you to enable/disable specific dice events, adjust their rarity, and add custom item rewards. *(Note: Set `useVersionedJson: false` in the TOML if you want to manually maintain a single JSON file across mod updates).*

---

## 🌍 Supported Languages

- 🇺🇸 English (`en_us`)
- 🇪🇸 Spanish (`es_es`)
- 🇫🇷 French (`fr_fr`)
- 🇩🇪 German (`de_de`)
- 🇷🇺 Russian (`ru_ru`)

*(Contributions for additional translations are always welcome!)*

---

## 🛠️ Development & Porting

This mod has been **successfully and completely ported** to NeoForge 1.21.1. The codebase has been modernized to leverage the latest Minecraft architecture, including:
- NeoForge `DeferredRegister` and Registry system.
- Modern `ItemAttributeModifiers` for accurate tooltip and combat stat calculations.
- NeoForge Data Components and Attachment Types for persistent entity data.
- Updated Curios API capability integration.
- Optimized custom models, textures, and client-side rendering.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE). 

---

## 👨‍💻 Credits

- **Lead Developer:** JrDemiurge (Manel)
- **Inspiration:** Epic random events, RPG mechanics, and unique Minecraft modding concepts.

---

## 🐛 Bugs and Suggestions

Found a bug or have an idea for a new dice event or item? 
Please [open an issue](https://github.com/JrDemiurg/Demis-Enigmatic-Dice/issues) on the GitHub repository or join the community Discord.

**Enjoy Enigmatic Dice!** 🎲✨
# Report Plugin

A simple and customizable Report Plugin for Minecraft servers.
This plugin allows players to quickly report issues, bugs, or rule violations directly in-game. Reports are stored and can be reviewed by staff through commands or a GUI.

**✨ Features**

Easy-to-use report system (/report <player> <reason>).

Configurable messages and permissions.

Lightweight and optimized for performance.

**📥 Installation**

Download the latest release from the Releases
page.

Place the .jar file into your server’s plugins folder.

Restart the server.

Enjoy it.

**⚡ Commands**
Command	Description	Permission
/report <player> <reason> →	Report a player with a reason
/report →	Toggle report alerts (only for staff)


**👮 Permissions**

- report.command.reload-config → The permission needed to reload every config
 
- report.staff.report → The permission needed to receive report alerts

- report.command.spectate → The permission needed to execute /spectate <player>

**🚀 Usage Example**

A player runs:

/report Notch Fly hacking in survival

Staff receives a notification in chat.

Staff can review with:

Ban by a button or spectate the player.

**📦 Requirements**

Java 17+

Spigot/Paper 1.21


**📝 License**

This project is licensed under the MIT License – see the LICENSE
 file for details.

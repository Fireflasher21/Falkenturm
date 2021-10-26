# Falkenturm
!!!Worldguard is needed to use Regions!!!Worldguard muss auf dem Server aktiv sein um Regions zu nutzen!!!

Zur Config: 
verify: Ob die Worldguard Region Funktion genutzt werden soll (False: Briefe können von überall verschickt werden und Briefkasten wird automatisch registriert)
to_worlds: Ob Briefe auch von anderen Welten aus geschickt werden können. Mit verify:true können damit Briefe nur von Festgelegten Regionen in dieser verschickt werden.
close: Ob die Bücher beim verschicken mit Spielernamen Verschlossen werden sollen
change_author: Nur aktiv wenn close:true. Spielername wird mit ??? ersetzt.

Chest: delete: Damit wird euch die Möglichkeit gegeben, die Kiste inaktive Spielern zu löschen. Den Zeitraum kann man selbst mit Time: angeben. Es wird in Tagen gerechnet.

Zu den Regions:
Um eine Poststelle oder Falkenturm zu erstellen, kann jede beliebige Region von Worldguard genutzt werden, welche den Custom Flag: Falkenturm im State Allow hat. Diese Flag wird automatisch beim Start mit eingebunden.
Zur Erstellung einer Region mit Worldguard bitte in den entsprechenden Docs nachlesen.

CustomResponse:
Dort können 90% der ausgegebenen Nachrichten bearbeitet werden.

Zu Spieler Dateien:
In diesen wird der First Login, Last Login und Last Logout gespeichert. Diese Daten nicht bearbeiten, da damit das alter der Kisten bestimmt wird. 
Unter Names ist zu sehen, ob bezüglicher Spieler gebannt ist, wie viele Namen er hatte und welcher von diesen im Moment aktiv ist. 

Setup:
Zuerst muss eine Region mittels Worldedit/Fawe/Worldguard erstellt werden. /regions define NAME
Danach muss dieser oder jeder anderen beliebigen Region der Flag: Falkenturm eingestellt werden: /rg flags -w "world" -p 6 NAME

Für Spieler:
Mit /bk add kann man mit einem Shift+Rechtsklick eine Kiste als Briefkasten registrieren. Wenn verify:false, wird die Position sofort gespeichert. Wenn verify:true muss der Briefkasten erst mit /bk verify in der Poststelle registriert werden
Mit /bk delete kann seinen existierenden Briefkasten löschen.
Mit /bk send NAME kann man einen Brief an alle Spieler senden, welche auf dem Server einen Briefkasten registriert haben. Gilt auch für Spieler, welche Offline sind. Sollte der Briefkasten voll sein wird der Brief nicht verschickt.
Alles weitere findet Ihr mit /bk help Befehl.

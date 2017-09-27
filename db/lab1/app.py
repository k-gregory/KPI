from lab1.sport import SportDB
from pickle import load, dump

class ExitEx(Exception):
    pass

def exitF():
    raise ExitEx()

def ask(p):
    return input(p).strip()

class Program:
    def add_team(self):
        self.db.team.add(
                name = ask("Team name: "),
                origin = ask("Team origin: "),
                sport = ask("Team sport: ")
                )

    def del_team(self):
        name = ask("Team name: ")
        self.db.team.delete(self.db.team.find(name=name))

    def del_player(self):
        id = ask("Player ID: ")
        self.db.player.delete(self.db.player.find(id=int(id)))

    def add_player(self):
        self.db.player.add(
                name = ask("Player name: "),
                team_name = ask("Player team: "),
                score = 0
                )
    def mod_player(self):
        id = int(ask("Player id: "))
        players = self.db.player.find(id=id)
        if not players:
            raise ValueError("Player not found")
        else:
            player = players[0]

        rewrite = {}
        for k,v in player.items():
            upd = ask("{}, default: '{}': ".format(k, v))
            if upd == "":
                rewrite[k] = v
            else:
                rewrite[k] = upd

        self.db.player.update(players, **rewrite)

    def mod_team(self):
        name = ask("Team name: ")
        teams = self.db.team.find(name=name)
        if not teams:
            raise ValueError("Team not found")
        else:
            team = teams[0]

        rewrite = {}
        for k,v in team.items():
            upd = ask("{}, default: {}: ".format(k, v))
            if upd == "":
                rewrite[k] = v
            else:
                rewrite[k] = upd

        self.db.team.update(teams, **rewrite)

    def exit():
        raise ExitEx()

    def print_help(self):
        print(list(self.commands.keys()))

    def print_best(self):
        print(self.db.best())

    def print_players(self):
        print(self.db.player.find())

    def print_teams(self):
        print(self.db.team.find())

    def load(self):
        with open("db.pk", "rb") as f:
            self.db = load(f)

    def save(self):
        with open("db.pk", "wb") as f:
            dump(self.db, f)


    def __init__(self):
        self.db = SportDB()
        self.commands = {
                "exit": exit,
                "help": self.print_help,
                "load": self.load,
                "save": self.save,
                "add team": self.add_team,
                "del team": self.del_team,
                "mod team": self.mod_team,
                "add player": self.add_player,
                "del player": self.del_player,
                "mod player": self.mod_player,
                "best": self.print_best,
                "teams": self.print_teams,
                "players": self.print_players
                }


    def run(self):
        try:
            while True:
                cmd = input(">> ").strip()
                try:
                    if cmd in self.commands:
                        self.commands[cmd]() 
                    else:
                        print("Command not found")
                except ValueError as e:
                    print(e)
        except ExitEx:
            pass

Program().run()

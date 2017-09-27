from lab1.data import Table, Attribute, autoinc

class SportDB:
    def __init__(self):
        self.team = Table(
                Attribute("name", unique=True),
                Attribute("origin"),
                Attribute("sport")
                )

        self.player = Table(
                Attribute("id", unique=True, default=autoinc()),
                Attribute("team_name", reference=self.team.reference("name")),
                Attribute("name"),
                Attribute("score")
                )

    def best(self):
        rs = []
        for team in self.team.find():
            players = self.player.find(team_name=team['name'])
            if not players:
                rs.append((team, None))
            else:
                rs.append((team,max(players, key=lambda p: int(p['score']))))

        return rs

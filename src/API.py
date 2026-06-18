from dataclasses import make_dataclass
import sqlite3
from typing import List
from Flag.Flag import Flag
import pygame
from Flag.Piece import Piece
from GlobalValues import Global


def dataclass_factory(cursor, row):
    fields = [column[0] for column in cursor.description]
    cls = make_dataclass("Row", fields)
    return cls(**{field: value for field, value in zip(fields, row)})


conn = sqlite3.connect(Global.DATABASE_PATH)
conn.row_factory = dataclass_factory


def get_flag_by_id(id: int):
    param = (id,)
    cursor = conn.execute(
        """
        select 
            f.id,
            f.popular_name,
            f.official_state_name,
            f.endonym,
            f.capital_city,
            r.name as region,
            ca.name as category,
            co.name as continent
        from 
            flags f
        left join regions r on r.id = f.region_id
        left join categories ca on ca.id = f.category_id
        left join Continents co on co.id = f.continent_id
        where f.id = ?
        """,
        param,
    )

    data = cursor.fetchone()
    pieces = get_flag_pieces(id)

    return Flag(
        id=data.id,
        popular_name=data.popular_name,
        official_state_name=data.official_state_name,
        endonym=data.endonym,
        capital_city=data.capital_city,
        region=data.region,
        category=data.category,
        continent=data.continent,
        pieces=pieces,
    )

def load_image(path: str) -> pygame.Surface:
    return pygame.image.load(path).convert_alpha()

def get_flag_pieces(id: int) -> List[Piece]:
    param = (id,)
    cursor = conn.execute(
        """
        select 
            pieces.id,
            pieces.position_x,
            pieces.position_y,
            pieces.asset_path
        from pieces
            where pieces.flag_id = ?
        """,
        param,
    )
    pieces = []
    for data in cursor.fetchall():
        piece = Piece(
            id=data.id,
            image=load_image(data.asset_path),
            flag_id=id
        )
        piece.rect.topleft = (data.position_x, data.position_y)
        pieces.append(piece)

    return pieces


def get_all_country_names() -> list[tuple[int, str]]:
    cursor = conn.execute("""
        select id, popular_name from flags
    """)
    return [(row.id, row.popular_name) for row in cursor.fetchall()]

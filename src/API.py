from dataclasses import make_dataclass
import sqlite3
from typing import List
from Flag.Flag import Flag
import pygame
from Flag.Piece import Piece
from GlobalValues import Global
from ClickableSprite import Clickable_sprite


def dataclass_factory(cursor, row):
    fields = [column[0] for column in cursor.description]
    cls = make_dataclass("Row", fields)
    return cls(**{field: value for field, value in zip(fields, row)})


conn = sqlite3.connect("src/database.db")
conn.row_factory = dataclass_factory


def get_flag(id: int):
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
        join regions r on r.id = f.region_id
        join categories ca on ca.id = f.category_id
        join Continents co on co.id = f.continent_id
        where f.id = ?
        """,
        param,
    )

    data: Flag = cursor.fetchone()
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


def load_sprite(asset_path: str):
    return Clickable_sprite(asset_path, Global.display_surf, asset_path)


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

    pieces: List[Piece] = [
        Piece(
            id=data.id,
            position=pygame.Vector2(data.position_x, data.position_y),
            asset_path=data.asset_path,
            flag_id=id,
            sprite=load_sprite(data.asset_path),
        )
        for data in cursor.fetchall()
    ]
    return pieces


def get_all_country_names() -> List[str | tuple[str, str]]:
    cursor = conn.execute("""
        select popular_name from flags
    """)
    country_names = [row.popular_name for row in cursor.fetchall()]
    print(country_names)
    return country_names

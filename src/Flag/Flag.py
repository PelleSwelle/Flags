from dataclasses import dataclass
from typing import List
from .Piece import Piece


@dataclass
class Flag:
    id: int
    popular_name: str
    official_state_name: str
    endonym: str
    capital_city: str
    region: str
    category: str
    continent: str
    pieces: List[Piece]

    def load_sprites(self):
        for piece in self.pieces:
            piece.load_sprite()

from typing import List
from ClickableSprite import Clickable_sprite
from Flag.Piece import Piece


class Controller:
    def select_next_piece(self, pieces: List[Piece]):
        for i, piece in enumerate(pieces):
            if piece.sprite.is_active:
                piece.sprite.is_active = False
                next_index = (i + 1) % len(pieces)
                pieces[next_index].sprite.is_active = True
                print(pieces[next_index].id)
                break

    def move_sprite(self, sprite: Clickable_sprite, x: int, y: int):
        print(f"move sprite {sprite.name} {x}, {y}")

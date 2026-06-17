from typing import List
import pygame
from ClickableSprite import Clickable_sprite
from dataclasses import dataclass
from GlobalValues import Global


@dataclass
class Piece:
    id: int
    position: pygame.Vector2
    asset_path: str
    flag_id: int
    sprite: None | Clickable_sprite

    def load_sprite(self):
       self.sprite = Clickable_sprite(image=self.asset_path, screen=Global.display_surf, name=self.asset_path) 


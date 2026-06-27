import pygame
from GlobalValues import Global
from dataclasses import dataclass, field
from random import randint


@dataclass
class Piece:
    id: int
    image: pygame.Surface
    correct_x: float
    correct_y: float
    asset_path: str
    rect: pygame.Rect = field(init=False)
    flag_id: int
    is_active: bool = False
    meaning: str = ""

    def __post_init__(self):
        self.rect = self.image.get_rect()
        self.rect.topleft = (
            randint(0, Global.SCREEN_WIDTH - self.rect.width),
            randint(0, Global.SCREEN_HEIGHT - self.rect.height),
        )

    def handle_keys(self, keys):
        if not self.is_active:
            return
        if keys[pygame.K_a]:
            print("move left")
            self.rect.x -= Global.MOVE_SPEED
        if keys[pygame.K_d]:
            print("move right")
            self.rect.x += Global.MOVE_SPEED
        if keys[pygame.K_w]:
            print("move up")
            self.rect.y -= Global.MOVE_SPEED
        if keys[pygame.K_s]:
            print("move down")
            self.rect.y += Global.MOVE_SPEED

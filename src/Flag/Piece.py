import pygame
from GlobalValues import Global
from dataclasses import dataclass, field

@dataclass
class Piece():
    id: int
    image: pygame.Surface 
    rect: pygame.Rect = field(init=False)
    flag_id: int
    is_active: bool = False

    def __post_init__(self):
        self.rect = self.image.get_rect()

    def handle_keys(self, keys):
        if not self.is_active:
            return
        if keys[pygame.K_a]:
            print("move left")
            self.rect.x -= 1
        if keys[pygame.K_d]:
            print("move right")
            self.rect.x += 1
        if keys[pygame.K_w]:
            print("move up")
            self.rect.y -= 1
        if keys[pygame.K_s]:
            print("move down")
            self.rect.y += 1
    
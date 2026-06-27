import pygame

class Global:
    SCREEN_WIDTH = 1200
    SCREEN_HEIGHT = 900
    DATABASE_PATH = "src/database.db"
    MOVE_SPEED = 3

    display_surf = pygame.display.set_mode(
        (SCREEN_WIDTH, SCREEN_HEIGHT), pygame.HWSURFACE | pygame.DOUBLEBUF
    )

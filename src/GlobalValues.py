import pygame


class Global:
    SCREEN_WIDTH = 1000
    SCREEN_HEIGHT = 800
    DATABASE_PATH = "src/database.db"

    display_surf = pygame.display.set_mode(
        (SCREEN_WIDTH, SCREEN_HEIGHT), pygame.HWSURFACE | pygame.DOUBLEBUF
    )

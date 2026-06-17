from typing import List
import pygame_gui
import pygame
from GlobalValues import Global
from API import get_all_country_names


class UI:
    pygame.font.init()
    manager = pygame_gui.UIManager((Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT))

    hello_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((350, 275), (100, 50)),
        text="Say Hello",
        manager=manager,
    )

    all_country_names = get_all_country_names()

    flags_dropdown = pygame_gui.elements.UIDropDownMenu(
        options_list=all_country_names,
        starting_option=all_country_names[0],
        relative_rect=pygame.Rect((100, 100), (200, 200)),
        manager=manager,
    )

    load_button = pygame_gui.elements.UIButton(
        relative_rect=pygame.Rect((0, 0), (20, 20)),
        text="load selected flag",
        manager=manager,
    )
